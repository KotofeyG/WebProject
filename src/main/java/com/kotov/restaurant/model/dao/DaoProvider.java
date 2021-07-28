package com.kotov.restaurant.model.dao;

public class DaoProvider {
    private UserDao1 userDao = new UserDaoImpl();
    private MealDao1 mealDao = new MealDaoImpl();
    private MenuDao menuDao = new MenuDaoImpl();

    private DaoProvider() {
    }

    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.instance;
    }

    public UserDao1 getUserDao() {
        return userDao;
    }

    public MealDao1 getMealDao() {
        return mealDao;
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }
}