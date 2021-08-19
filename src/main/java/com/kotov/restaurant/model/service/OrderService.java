package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Order;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> findOrdersByUserId(long userId) throws ServiceException;

    List<Order> findOrdersByStatuses(EnumSet<Order.Status> statuses) throws ServiceException;

    boolean insertOrder(Map<String, String> orderedMeals, long userId, String addressIdStr, String time, String paymentType) throws ServiceException;

    boolean updateOrderStatus(String orderIdStr, Order.Status status) throws ServiceException;
}