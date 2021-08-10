package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException;

    List<User> findAllUsers() throws ServiceException;

    List<Address> findUserAddresses(long userId) throws ServiceException;

    boolean updateUserStatusesById(String statusStr, String[] userIdArray) throws ServiceException;

    boolean deleteUsersById(String[] userIdArray) throws ServiceException;

    boolean registerNewUser(Map<String, String> dataCheckResult) throws ServiceException;

    Map<Meal, Integer> findMealsInCartByUserId(long userId) throws ServiceException;

    boolean deleteMealsFromCartByUserId(long userId, String[] mealIdArray) throws ServiceException;

    boolean addUserAddress(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean addDiscountCardToUser(long userId, String number) throws ServiceException;

    boolean changeUserPersonalData(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean changeAccountPassword(long userId, Map<String, String> dataCheckResult) throws ServiceException;
}