package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.OrderDao;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();

    @Override
    public boolean addOrder(long userId, Order order) throws DaoException {
        return false;
    }

    @Override
    public Optional<Order> findEntityById(long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public List<Order> findAllEntities() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insertNewEntity(Order entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteEntityById(long id) throws DaoException {
        return false;
    }

    @Override
    public void deleteEntitiesById(List<Long> idList) throws DaoException {
        throw new UnsupportedOperationException();
    }
}