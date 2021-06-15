package com.kotov.sushi_bar.model.pool;

import com.kotov.sushi_bar.exception.DatabaseConnectionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum ConnectionPool {
    INSTANCE;

    private static final Logger logger = LogManager.getLogger();
    private BlockingQueue<ProxyConnection> freeConnections;
    private Queue<ProxyConnection> busyConnections;

    ConnectionPool() {
        int poolSize = ConnectionCreator.getPoolSize();
        freeConnections = new LinkedBlockingQueue<>(poolSize);
        busyConnections = new ArrayDeque<>();
        for (int i = 0; i < poolSize; i++) {
            Connection connection = null;
            try {
                connection = ConnectionCreator.getConnection();
            } catch (DatabaseConnectionException e) {
                //log
            }
            ProxyConnection proxyConnection = new ProxyConnection(connection);
            freeConnections.offer(proxyConnection);
        }
    }

    public Connection getConnection() throws DatabaseConnectionException {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
        } catch (InterruptedException e) {
            throw new DatabaseConnectionException("Impossible to connect to a database");
        }
        busyConnections.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection proxyConnection && busyConnections.remove(proxyConnection)) {
            try {
                freeConnections.put(proxyConnection);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Connection is interrupted while waiting", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void destroyPool() {
        int poolSize = ConnectionCreator.getPoolSize();
        for (int i = 0; i < poolSize; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Unable to close connection");
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Thread was interrupted while taking a free connection", e);
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