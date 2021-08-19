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

    Optional<User> findUserById(long id) throws ServiceException;

    List<Address> findUserAddresses(long userId) throws ServiceException;

    Map<Meal, Integer> findMealsInCartByUserId(long userId) throws ServiceException;

    boolean updateUserStatusesById(User.Status status, String[] userIdArray) throws ServiceException;

    boolean updateFirstNameById(long userId, String firstName) throws ServiceException;

    boolean updatePatronymicById(long userId, String patronymic) throws ServiceException;

    boolean updateLastNameById(long userId, String lastName) throws ServiceException;

    boolean updateMobileNumberById(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean updateEmailById(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean updateAccountPassword(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean registerNewUser(Map<String, String> dataCheckResult) throws ServiceException;

    boolean insertUserAddress(long userId, Map<String, String> dataCheckResult) throws ServiceException;

    boolean deleteUsersById(String[] userIdArray) throws ServiceException;

    boolean deleteMealsFromCartByUserId(long userId, String[] mealIdArray) throws ServiceException;
}