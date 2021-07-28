package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean checkLogin(String login) throws DaoException;

    boolean checkEmail(String email) throws DaoException;

    boolean checkMobileNumber(String mobileNumber) throws DaoException;

    Optional<User> findUserByLoginAndPassword(String login, String passwordHah)  throws DaoException;

    List<User> findAllUsers() throws DaoException;

    void addNewUser(User user, String passwordHash) throws DaoException;
}