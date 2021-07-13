package com.kotov.restaurant.model.pool;

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
    private static final int ONCE = 1;
    private static final int POOL_INITIALIZATION_ATTEMPTS_NUMBER = 3;
    private static final long DELAY_UNTIL_REMAINING_THREADS_FINISH = 100;
    private static final long DELAY_UNTIL_CONNECTIONS_NUMBER_CHECK = 1;
    private static final long PERIOD_BETWEEN_CONNECTIONS_NUMBER_CHECK = 1;
    private static final long MAX_CONNECTION_TIMEOUT = 10;

    private static ConnectionPool instance;
    private static CountDownLatch initialisingLatch = new CountDownLatch(ONCE);
    private static AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);

    private Timer poolSizeCheckTimer;
    private CountDownLatch connectionsCheckLatch;
    private AtomicBoolean connectionsNumberCheck = new AtomicBoolean(false);

    private Semaphore semaphore;

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> busyConnections;

    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        busyConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        semaphore = new Semaphore(POOL_SIZE);
        int attemptCounter = 0;
        while (freeConnections.size() < POOL_SIZE && attemptCounter < POOL_INITIALIZATION_ATTEMPTS_NUMBER) {
            int currentPoolSize = freeConnections.size();
            for (int i = currentPoolSize; i < POOL_SIZE; i++) {
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
        poolSizeCheckTimer = new Timer();
        poolSizeCheckTimer.schedule(new TimerConnectionCounter()
                , TimeUnit.HOURS.toMillis(DELAY_UNTIL_CONNECTIONS_NUMBER_CHECK)
                , TimeUnit.HOURS.toMillis(PERIOD_BETWEEN_CONNECTIONS_NUMBER_CHECK));
        logger.log(Level.INFO, "Connection pool was created");
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
                logger.log(Level.ERROR, "Thread is interrupted while ConnectionPool is creating", e);
                Thread.currentThread().interrupt();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connectionsNumberCheck.get()) {
                connectionsCheckLatch.await();
            }
            if (semaphore.tryAcquire(MAX_CONNECTION_TIMEOUT, TimeUnit.SECONDS)) {
                ProxyConnection connection = freeConnections.take();
                busyConnections.put(connection);
                logger.log(Level.DEBUG, "Connection was taken from pool");
                return connection;
            }
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Impossible to get connection", e);
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
        throw new RuntimeException("Impossible to get connection as free connection timed out");
    }

    public boolean releaseConnection(Connection connection) {
        boolean result = true;
        try {
            if (connectionsNumberCheck.get()) {
                connectionsCheckLatch.await();
            }
            if (connection instanceof ProxyConnection proxyConnection && busyConnections.remove(proxyConnection)) {
                freeConnections.put(proxyConnection);
            } else {
                logger.log(Level.WARN, "Impossible to return connection into pool. Wrong argument: "
                        + connection);
                result = false;
            }
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Impossible to return connection into pool", e);
            Thread.currentThread().interrupt();
        }
        logger.log(Level.DEBUG, "Connection was returned into pool");
        return result;
    }

    void addMissingConnections() {
        int currentConnectionsNumber = POOL_SIZE;
        try {
            connectionsCheckLatch = new CountDownLatch(ONCE);
            connectionsNumberCheck.set(true);
            TimeUnit.MILLISECONDS.sleep(DELAY_UNTIL_REMAINING_THREADS_FINISH);
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
            logger.log(Level.WARN, "Insufficient number of connections in the pool: Current size is "
                    + currentConnectionsNumber + ", required " + POOL_SIZE);
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
        logger.log(Level.INFO, "Connection was created and added to pool");
    }

    public void destroyPool() {
        poolSizeCheckTimer.cancel();
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
        logger.log(Level.INFO, "Connection pool was destroyed");
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
        logger.log(Level.INFO, "Database drivers were de-registered");
    }
}