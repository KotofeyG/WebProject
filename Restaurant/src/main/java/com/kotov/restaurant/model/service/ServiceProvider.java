package com.kotov.restaurant.model.service;

import com.kotov.restaurant.model.service.impl.MenuServiceImpl;
import com.kotov.restaurant.model.service.impl.OrderServiceImpl;
import com.kotov.restaurant.model.service.impl.UserServiceImpl;

public class ServiceProvider {
    private UserService userService = new UserServiceImpl();
    private MenuService menuService = new MenuServiceImpl();
    private OrderService orderService = new OrderServiceImpl();

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

    public MenuService getMenuService() {
        return menuService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}