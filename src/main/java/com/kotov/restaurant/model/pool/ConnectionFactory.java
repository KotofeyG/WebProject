package com.kotov.restaurant.model.pool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();
    private static final Properties properties = new Properties();
    private static final String DATABASE_URL;
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_POOL_SIZE = "pool.size";
    private static final String PROPERTIES_PATH = "config/db.properties";
    private static final int DEFAULT_POOL_SIZE = 16;
    static final int POOL_SIZE;

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(inputStream);
            String driverName = properties.getProperty(DB_DRIVER);
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.log(Level.FATAL, "Unable to load application driver", e);
            throw new ExceptionInInitializerError("Unable to load application driver");
        } catch (NullPointerException | IOException e) {
            logger.log(Level.FATAL, "Property file is not found", e);
            throw new ExceptionInInitializerError("Property file is not found");
        }
        DATABASE_URL = properties.getProperty(DB_URL);
        int tempSize;
        try {
            tempSize = Integer.parseInt(properties.getProperty(DB_POOL_SIZE));
        } catch (NumberFormatException e) {
            logger.log(Level.ERROR, "String does not contain a parsable integer for POOL_SIZE variable", e);
            tempSize = DEFAULT_POOL_SIZE;
        }
        POOL_SIZE = tempSize;
    }

    private ConnectionFactory() {
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, properties);
    }
}