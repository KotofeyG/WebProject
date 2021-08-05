package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;

public interface MealService {
    boolean insertMealToUserCart(long userId, String mealIdStr, String mealQuantityStr) throws ServiceException;
}