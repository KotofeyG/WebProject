package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public interface OrderDao extends BaseDao<Order> {
    boolean addMealsToOrder(long orderId, Map<Meal, Integer> orderedMeals) throws DaoException;

    List<Order> findOrdersByStatus(Order.Status status) throws DaoException;

    List<Order> findOrdersByUserId(long userId) throws DaoException;

    List<Order> findOrdersByStatuses(EnumSet<Order.Status> statuses) throws DaoException;

    Map<Meal, Integer> findMealsForOrder(long orderId) throws DaoException;

    boolean updateOrderStatus(long orderId, Order.Status status) throws DaoException;
}