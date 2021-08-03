package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.model.dao.impl.MealDaoImpl;
import com.kotov.restaurant.model.dao.impl.MenuDaoImpl;
import com.kotov.restaurant.model.dao.impl.UserDaoImpl;

public class DaoProvider {
    private UserDao userDao = new UserDaoImpl();
    private MealDao mealDao = new MealDaoImpl();
    private MenuDao menuDao = new MenuDaoImpl();

    private DaoProvider() {
    }

    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.instance;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public MealDao getMealDao() {
        return mealDao;
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }
}