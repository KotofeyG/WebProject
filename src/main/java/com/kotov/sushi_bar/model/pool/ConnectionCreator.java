package com.kotov.sushi_bar.model.pool;

import com.kotov.sushi_bar.exception.DatabaseConnectionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionCreator {
    private static final Logger logger = LogManager.getLogger();
    private static final Properties properties = new Properties();
    private static final String DATABASE_URL;
    private static final int POOL_SIZE;
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_POOL_SIZE = "pool.size";
    private static final String PROPERTIES_PATH = "config/db.properties";


    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(inputStream);
            String driverName = properties.getProperty(DB_DRIVER);
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.log(Level.FATAL, "Unable to load application driver");
            throw new RuntimeException("Unable to load application driver", e);
        } catch (IOException e) {
            logger.log(Level.FATAL, "Property file is not found");
            throw new RuntimeException("Property file is not found", e);
        }
        DATABASE_URL = properties.getProperty(DB_URL);
        POOL_SIZE = Integer.parseInt(properties.getProperty(DB_POOL_SIZE));
    }

    private ConnectionCreator() {
    }

    static Connection getConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Unable to create connection", e);
        }
    }

    static int getPoolSize() {
        return POOL_SIZE;
    }
}