package com.kotov.bar.model.pool;

import com.kotov.bar.exception.DatabaseConnectionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.kotov.bar.model.pool.ConnectionFactory.POOL_SIZE;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance;
    private static AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> busyConnections;

    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        busyConnections = new LinkedBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection = null;
            try {
                connection = ConnectionFactory.getConnection();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Unable to create connection", e);
            }
            ProxyConnection proxyConnection = new ProxyConnection(connection);
            freeConnections.add(proxyConnection);
        }
        if (freeConnections.size() < POOL_SIZE) {
            logger.log(Level.FATAL, "Not enough connections was created. Required " + POOL_SIZE + ", but got "
                    + freeConnections.size());
            throw new RuntimeException("Not enough connections was created. Required " + POOL_SIZE + ", but got "
                    + freeConnections.size());
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            while (isInstanceInitialized.compareAndSet(false, true)) {
                instance = new ConnectionPool();
            }
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                throw new DatabaseConnectionException("Thread is interrupted while ConnectionPool is filling", e);
//            }
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseConnectionException {
        try {
            ProxyConnection connection = freeConnections.take();
            busyConnections.put(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new DatabaseConnectionException("Impossible to get connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection proxyConnection && busyConnections.remove(proxyConnection)) {
            try {
                freeConnections.put(proxyConnection);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Impossible to return connection into pool", e);
                Thread.currentThread().interrupt();
            }
        } else {
            logger.log(Level.WARN, "Impossible to return connection into pool. Wrong argument: " + connection);
        }
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