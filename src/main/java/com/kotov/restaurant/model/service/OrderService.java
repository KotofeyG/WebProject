package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;

import java.util.Map;

public interface OrderService {
    boolean addOrder(long userId, Map<String, String> orderedMealsStr) throws ServiceException;
}