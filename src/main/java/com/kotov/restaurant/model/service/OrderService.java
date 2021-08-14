package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Optional<Order> findOrderById(String orderIdStr) throws ServiceException;

    List<Order> findOrdersByUserId(long userId) throws ServiceException;

    boolean addOrder(Map<String, String> orderedMealsStr, long userId, String addressIdStr, String time, String paymentType) throws ServiceException;

    List<Order> findOrdersByStatus(Order.Status status) throws ServiceException;

    boolean changeOrderStatus(String orderIdStr, Order.Status status) throws ServiceException;
}