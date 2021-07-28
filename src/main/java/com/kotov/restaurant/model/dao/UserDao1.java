package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.User;

import java.util.Optional;

public interface UserDao1 extends BaseDao<User> {
    boolean isLoginExist(String login) throws DaoException;

    boolean isEmailExist(String email) throws DaoException;

    boolean isMobileNumberExist(String mobileNumber) throws DaoException;

    Optional<User> findUserByLoginAndPassword(String login, String passwordHah)  throws DaoException;

    long insertNewEntity(User user, String passwordHash) throws DaoException;
}