package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException;

    List<User> findAllUsers() throws ServiceException;

    boolean updateUserStatusesById(String statusStr, String[] userIdArray) throws ServiceException;

    boolean deleteUsersById(String[] userIdArray) throws ServiceException;

    boolean registerNewUser(Map<String, String> dataCheckResult) throws ServiceException;
}