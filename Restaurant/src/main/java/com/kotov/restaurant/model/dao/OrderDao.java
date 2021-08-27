package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderDao extends BaseDao<Order> {

    /**
     * @param orderId {@link Order}s id
     * @return true if the order in cash, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isOrderInCash(long orderId) throws DaoException;

    /**
     * @param userId with which {@link Order}s will be found
     * @return collection of {@link Order}s
     * @throws DaoException if the request to data base could not be handled
     */
    List<Order> findOrdersByUserId(long userId) throws DaoException;

    /**
     * @param statuses {@link Order} statuses which used for searching
     * @return collection of {@link Order}s
     * @throws DaoException if the request to data base could not be handled
     */
    List<Order> findOrdersByStatuses(EnumSet<Order.Status> statuses) throws DaoException;

    /**
     * @param orderId {@link Order}s id
     * @return collection of {@link Meal}s and their quantity
     * @throws DaoException if the request to data base could not be handled
     */
    Map<Meal, Integer> findMealsForOrder(long orderId) throws DaoException;

    /**
     * @param orderId which used for searching
     * @return Optional sought order status
     * @throws DaoException if the request to data base could not be handled
     */
    Optional<Order.Status> findOrderStatus(long orderId) throws DaoException;

    /**
     * @param orderId {@link Order}s id
     * @param status  {@link Order}s status
     * @return true if status was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateOrderStatus(long orderId, Order.Status status) throws DaoException;
}