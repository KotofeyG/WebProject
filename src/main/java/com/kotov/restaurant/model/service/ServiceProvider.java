package com.kotov.restaurant.model.service;

import com.kotov.restaurant.model.service.impl.MealServiceImpl;
import com.kotov.restaurant.model.service.impl.UserServiceImpl;

public class ServiceProvider {
    private UserService userService = new UserServiceImpl();
    private MealService mealService = new MealServiceImpl();

    private ServiceProvider() {
    }

    private static class ServiceProviderHolder {
        private static final ServiceProvider instance = new ServiceProvider();
    }

    public static ServiceProvider getInstance() {
        return ServiceProviderHolder.instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public MealService getMealService() {
        return mealService;
    }
}