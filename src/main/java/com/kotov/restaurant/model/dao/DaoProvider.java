package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.model.dao.impl.UserDaoImpl;

public class DaoProvider {
    private UserDao userDao = new UserDaoImpl();

    private DaoProvider() {
    }

    private static class DaoProviderHolder {
        private static final DaoProvider INSTANCE = new DaoProvider();
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.INSTANCE;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}