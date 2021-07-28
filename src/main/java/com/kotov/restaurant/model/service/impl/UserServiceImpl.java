package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.UserDao1;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.model.service.util.PasswordEncryptor;
import com.kotov.restaurant.model.service.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static java.lang.Boolean.parseBoolean;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final UserDao1 dao = DaoProvider.getInstance().getUserDao();

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException {
        Optional<User> user = Optional.empty();
        try {
            if (UserValidator.isLoginValid(login) && UserValidator.isPasswordValid(password)) {
                String passwordHash = PasswordEncryptor.encrypt(password);
                user = dao.findUserByLoginAndPassword(login, passwordHash);
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user by login and password: ", e);
            throw new ServiceException("Impossible to find user by login and password: ", e);
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        try {
            return dao.findAllEntities();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find all users: ", e);
            throw new ServiceException("Impossible to find all users: ", e);
        }
    }

    @Override
    public boolean registerNewUser(Map<String, String> dataCheckResult) throws ServiceException {
        boolean result;
        String login = dataCheckResult.get(LOGIN);
        String password = dataCheckResult.get(PASSWORD);
        String confirmPassword = dataCheckResult.get(CONFIRM_PASSWORD);
        String email = dataCheckResult.get(EMAIL);
        String mobileNumber = dataCheckResult.get(MOBILE_NUMBER);

        try {
            String loginCheckResult = UserValidator.isLoginValid(login)
                    ? (!dao.isLoginExist(login) ? TRUE : NOT_UNIQUE_LOGIN_MESSAGE)
                    : INVALID_LOGIN_MESSAGE;
            String passwordCheckResult = UserValidator.isPasswordValid(password)
                    ? (password.equals(confirmPassword) ? TRUE : PASSWORD_MISMATCH_MESSAGE)
                    : INVALID_PASSPORT_MESSAGE;
            String emailCheckResult = UserValidator.isEmailValid(email)
                    ? (!dao.isEmailExist(email) ? TRUE : NOT_UNIQUE_EMAIL_MESSAGE)
                    : INVALID_EMAIL_MESSAGE;
            String mobileNumberCheckResult = UserValidator.isMobileNumberValid(mobileNumber)
                    ? (!dao.isMobileNumberExist(mobileNumber) ? TRUE : NOT_UNIQUE_MOBILE_NUMBER_MESSAGE)
                    : INVALID_MOBILE_NUMBER_MESSAGE;

            if (parseBoolean(loginCheckResult) && parseBoolean(passwordCheckResult)
                    && parseBoolean(emailCheckResult) && parseBoolean(mobileNumberCheckResult)) {
                User user = new User(login, email, mobileNumber, LocalDateTime.now());
                String passwordHash = PasswordEncryptor.encrypt(password);
                dao.insertNewEntity(user, passwordHash);
                result = true;
            } else {
                dataCheckResult.replace(LOGIN, loginCheckResult);
                dataCheckResult.replace(PASSWORD, passwordCheckResult);
                dataCheckResult.remove(CONFIRM_PASSWORD, confirmPassword);
                dataCheckResult.replace(EMAIL, emailCheckResult);
                dataCheckResult.replace(MOBILE_NUMBER, mobileNumberCheckResult);
                result = false;
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to create new user: ", e);
            throw new ServiceException("Impossible to create new user: ", e);
        }
        return result;
    }
}