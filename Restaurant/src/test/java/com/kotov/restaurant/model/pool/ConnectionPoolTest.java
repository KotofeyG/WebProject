package com.kotov.restaurant.model.pool;

import com.kotov.restaurant.exception.DaoException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;

public class ConnectionPoolTest {
    ConnectionPool connectionPool;
    Connection connection;

    @BeforeClass
    public void before() throws DaoException {
        connectionPool = ConnectionPool.getInstance();
        connection = connectionPool.getConnection();
    }

    @Test
    public void testGetInstance() {
        Assert.assertNotNull(connection);
        connectionPool.releaseConnection(connection);
    }
}