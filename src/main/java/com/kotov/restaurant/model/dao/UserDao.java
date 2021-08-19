package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends BaseDao<User> {
    boolean isLoginExist(String login) throws DaoException;

    boolean isEmailExist(String email) throws DaoException;

    boolean isMobileNumberExist(String mobileNumber) throws DaoException;

    boolean isUserExist(long userId, String passwordHash) throws DaoException;

    Optional<User> findUserByLoginAndPassword(String login, String passwordHah)  throws DaoException;

    Optional<Address> findAddressById(long addressId) throws DaoException;

    List<Address> findUserAddresses(long userId) throws DaoException;

    Map<Meal, Integer> findMealsInCartByUserId(long userId) throws DaoException;

    boolean insertUserAddress(long userId, Address address) throws DaoException;

    long insertNewEntity(User user, String passwordHash) throws DaoException;

    boolean updateUserFirstName(long userId, String firstName) throws DaoException;

    boolean updateUserPatronymic(long userId, String patronymic) throws DaoException;

    boolean updateUserLastName(long userId, String lastName) throws DaoException;

    boolean updateUserMobileNumber(long userId, String mobileNumber) throws DaoException;

    boolean updateUserEmail(long userId, String email) throws DaoException;

    boolean updateUserPassword(long userId, String passwordHash) throws DaoException;

    boolean updateUserStatusesById(User.Status status, List<Long> userIdList) throws DaoException;

    boolean deleteMealsFromCartByUserId(long userId, List<Long> mealIdList) throws DaoException;
}