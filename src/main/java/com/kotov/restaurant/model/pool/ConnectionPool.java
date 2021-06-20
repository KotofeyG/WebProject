package com.kotov.restaurant.model.pool;

import com.kotov.restaurant.exception.DatabaseConnectionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.kotov.restaurant.model.pool.ConnectionFactory.POOL_SIZE;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance;
    private static CountDownLatch initialisingLatch = new CountDownLatch(1);
    private static AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);
    private static final int POOL_INITIALIZATION_ATTEMPTS_NUMBER = 3;

    private static final long DELAY_UNTIL_CONNECTIONS_NUMBER_CHECK = 1;
    private static final long PERIOD_BETWEEN_CONNECTIONS_NUMBER_CHECK = 1;
    private CountDownLatch connectionsCheckLatch;
    private AtomicBoolean connectionsNumberCheck = new AtomicBoolean(false);

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> busyConnections;

    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        busyConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        int attemptCounter = 0;
        while (freeConnections.size() < POOL_SIZE && attemptCounter < POOL_INITIALIZATION_ATTEMPTS_NUMBER) {
            for (int i = 0; i < POOL_SIZE; i++) {
                addConnectionToPool();
            }
            attemptCounter++;
        }
        if (freeConnections.size() < POOL_SIZE) {
            logger.log(Level.FATAL, "Not enough connections was created. Required " + POOL_SIZE + ", but got "
                    + freeConnections.size());
            throw new RuntimeException("Not enough connections was created. Required " + POOL_SIZE + ", but got "
                    + freeConnections.size());
        }
        Timer timer = new Timer();
        timer.schedule(new TimerConnectionCounter()
                , TimeUnit.HOURS.toMillis(DELAY_UNTIL_CONNECTIONS_NUMBER_CHECK)
                , TimeUnit.HOURS.toMillis(PERIOD_BETWEEN_CONNECTIONS_NUMBER_CHECK));
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            while (isInstanceInitialized.compareAndSet(false, true)) {
                instance = new ConnectionPool();
                initialisingLatch.countDown();
            }
            try {
                initialisingLatch.await();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Thread is interrupted while ConnectionPool is filling", e);
                Thread.currentThread().interrupt();
            }
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseConnectionException {
        try {
            if (connectionsNumberCheck.get()) {
                connectionsCheckLatch.await();
            }
            ProxyConnection connection = freeConnections.take();
            busyConnections.put(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new DatabaseConnectionException("Impossible to get connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        try {
            if (connectionsNumberCheck.get()) {
                connectionsCheckLatch.await();
            }
            if (connection instanceof ProxyConnection proxyConnection && busyConnections.remove(proxyConnection)) {
                freeConnections.put(proxyConnection);
            } else {
                logger.log(Level.WARN, "Impossible to return connection into pool. Wrong argument: " + connection);
            }
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Impossible to return connection into pool", e);
            Thread.currentThread().interrupt();
        }
    }

    void addMissingConnections() {
        int currentConnectionsNumber = POOL_SIZE;
        try {
            connectionsCheckLatch = new CountDownLatch(1);
            connectionsNumberCheck.set(true);
            TimeUnit.MICROSECONDS.sleep(100);
            currentConnectionsNumber = freeConnections.size() + busyConnections.size();
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, Thread.class.getSimpleName()
                    + " thread was interrupted while checking free connection", e);
            Thread.currentThread().interrupt();
        } finally {
            connectionsNumberCheck.set(false);
            connectionsCheckLatch.countDown();
        }
        if (currentConnectionsNumber < POOL_SIZE) {
            int requiredConnectionsNumber = POOL_SIZE - (freeConnections.size() + busyConnections.size());
            for (int i = 0; i < requiredConnectionsNumber; i++) {
                addConnectionToPool();
            }
        }
    }

    private void addConnectionToPool() {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Unable to create connection", e);
        }
        ProxyConnection proxyConnection = new ProxyConnection(connection);
        freeConnections.add(proxyConnection);
    }

    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                ProxyConnection connection = freeConnections.take();
                connection.reallyClose();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Unable to close connection", e);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Thread was interrupted while taking free connection", e);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.getDrivers()
                .asIterator()
                .forEachRemaining(driver -> {
                    try {
                        DriverManager.deregisterDriver(driver);
                    } catch (SQLException e) {
                        logger.log(Level.ERROR, "Impossible deregister driver", e);
                    }
                });
    }
}