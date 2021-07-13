package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.RegistrationData;
import com.kotov.restaurant.model.entity.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    Map<String, String> checkRegistrationParams(String... params) throws ServiceException;

    Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException;

    void registerNewUser(RegistrationData data, String password) throws ServiceException;
}