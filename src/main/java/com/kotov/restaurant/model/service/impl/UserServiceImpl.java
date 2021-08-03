package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.model.service.util.PasswordEncryptor;
import com.kotov.restaurant.model.service.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Boolean.parseBoolean;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final UserDao userDao = DaoProvider.getInstance().getUserDao();

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException {
        Optional<User> optionalUser = Optional.empty();
        try {
            if (UserValidator.isLoginValid(login) && UserValidator.isPasswordValid(password)) {
                String passwordHash = PasswordEncryptor.encrypt(password);
                optionalUser = userDao.findUserByLoginAndPassword(login, passwordHash);
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user by login and password: ", e);
            throw new ServiceException("Impossible to find user by login and password: ", e);
        }
        return optionalUser;
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        try {
            List<User> users = userDao.findAllEntities();
            for (User user : users) {
                Optional<Address> addressOptional = userDao.findAddressByUserId(user.getId());
                addressOptional.ifPresent(user::setAddress);
            }
            return users;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find all users: ", e);
            throw new ServiceException("Impossible to find all users: ", e);
        }
    }

    @Override
    public boolean updateUserStatusesById(String statusStr, String[] userIdArray) throws ServiceException {
        boolean result = false;
        if (userIdArray != null) {
            try {
                List<Long> userIdList = convertArrayToList(userIdArray);
                User.Status status = User.Status.valueOf(statusStr);
                userDao.updateUserStatusesById(status, userIdList);
                result = true;
                logger.log(Level.DEBUG, "updateUserStatuses service method is completed successfully. Result is: " + result);
            } catch (DaoException | IllegalArgumentException e) {
                logger.log(Level.ERROR, "Impossible to update user statuses:", e);
                throw new ServiceException("Impossible to update user statuses:", e);
            }
        }
        return result;
    }

    @Override
    public boolean deleteUsersById(String[] userIdArray) throws ServiceException {
        boolean result = false;
        if (userIdArray != null) {
            try {
                List<Long> userIdList = convertArrayToList(userIdArray);
                userDao.deleteEntitiesById(userIdList);
                result = true;
                logger.log(Level.DEBUG, "removeUsers service method is completed successfully. Result is: " + result);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Impossible to remove users:", e);
                throw new ServiceException("Impossible to remove users:", e);
            }
        }
        return result;
    }

    private List<Long> convertArrayToList(String[] idArray) throws ServiceException {
        List<Long> idList = new ArrayList<>();
        try {
            for (String idStr : idArray) {
                idList.add(Long.parseLong(idStr));
            }
            return idList;
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Variable idStr does not contain a parsable long:", e);
            throw new ServiceException("Variable idStr does not contain a parsable long:", e);
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
        String roleStr = dataCheckResult.get(USER_ROLE);

        try {
            String loginCheckResult = UserValidator.isLoginValid(login)
                    ? (!userDao.isLoginExist(login) ? TRUE : NOT_UNIQUE_LOGIN_MESSAGE)
                    : INVALID_LOGIN_MESSAGE;
            String passwordCheckResult = UserValidator.isPasswordValid(password)
                    ? (password.equals(confirmPassword) ? TRUE : PASSWORD_MISMATCH_MESSAGE)
                    : INVALID_PASSPORT_MESSAGE;
            String emailCheckResult = UserValidator.isEmailValid(email)
                    ? (!userDao.isEmailExist(email) ? TRUE : NOT_UNIQUE_EMAIL_MESSAGE)
                    : INVALID_EMAIL_MESSAGE;
            String mobileNumberCheckResult = UserValidator.isMobileNumberValid(mobileNumber)
                    ? (!userDao.isMobileNumberExist(mobileNumber) ? TRUE : NOT_UNIQUE_MOBILE_NUMBER_MESSAGE)
                    : INVALID_MOBILE_NUMBER_MESSAGE;

            if (parseBoolean(loginCheckResult) && parseBoolean(passwordCheckResult)
                    && parseBoolean(emailCheckResult) && parseBoolean(mobileNumberCheckResult)) {
                User.Role role;
                if (roleStr != null) {
                    role = User.Role.valueOf(roleStr.toUpperCase());
                } else {
                    role = User.Role.CLIENT;
                }
                User user = new User(login, email, mobileNumber, LocalDateTime.now(), role);
                String passwordHash = PasswordEncryptor.encrypt(password);
                userDao.insertNewEntity(user, passwordHash);
                result = true;
            } else {
                dataCheckResult.remove(USER_ROLE, roleStr);
                dataCheckResult.remove(CONFIRM_PASSWORD, confirmPassword);
                dataCheckResult.replace(LOGIN, loginCheckResult);
                dataCheckResult.replace(PASSWORD, passwordCheckResult);
                dataCheckResult.replace(EMAIL, emailCheckResult);
                dataCheckResult.replace(MOBILE_NUMBER, mobileNumberCheckResult);
                result = false;
            }
        } catch (DaoException | IllegalArgumentException e) {
            logger.log(Level.ERROR, "Impossible to create new user: ", e);
            throw new ServiceException("Impossible to create new user: ", e);
        }
        return result;
    }
}