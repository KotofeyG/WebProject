package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Cart;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends BaseDao<User> {
    boolean isLoginExist(String login) throws DaoException;

    boolean isEmailExist(String email) throws DaoException;

    boolean isMobileNumberExist(String mobileNumber) throws DaoException;

    Optional<User> findUserByLoginAndPassword(String login, String passwordHah)  throws DaoException;

    Optional<Address> findAddressByUserId(long userId) throws DaoException;

    long insertNewEntity(User user, String passwordHash) throws DaoException;

    void updateUserStatusesById(User.Status status, List<Long> userIdList) throws DaoException;

    Cart findUserMealsInCart(long userId) throws DaoException;
}