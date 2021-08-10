package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Order;

public interface OrderDao extends BaseDao<Order>{
    boolean addOrder(long userId, Order order) throws DaoException;
}