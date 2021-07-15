package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.RegistrationData;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.util.PasswordEncryptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.kotov.restaurant.command.ParamName.*;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final DaoProvider daoProvider = DaoProvider.getInstance();

    private UserServiceImpl() {
    }

    private static class UserServiceImplHolder {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }

    public static UserServiceImpl getInstance() {
        return UserServiceImplHolder.INSTANCE;
    }

    @Override
    public Map<String, String> checkRegistrationParams(String... params) throws ServiceException {
        UserDao dao = daoProvider.getUserDao();
        Map<String, String> dataCheckResult = new HashMap<>();

        String login = params[LOGIN_INDEX];
        String password = params[PASSWORD_INDEX];
        String confirmPassword = params[CONFIRM_PASSWORD_INDEX];
        String email = params[EMAIL_INDEX];
        String mobileNumber = params[MOBILE_NUMBER_INDEX];

        try {
            String loginCheckResult = login.matches(LOGIN_REGEX)
                    ? (!dao.checkLogin(login) ? VALID : NOT_UNIQUE_LOGIN_MESSAGE)
                    : INVALID_LOGIN_MESSAGE;
            String passwordCheckResult = password.matches(PASSPORT_REGEX)
                    ? (password.equals(confirmPassword) ? VALID : PASSWORD_MISMATCH_MESSAGE)
                    : INVALID_PASSPORT_MESSAGE;
            String emailCheckResult = email.matches(EMAIL_REGEX)
                    ? (!dao.checkEmail(email) ? VALID : NOT_UNIQUE_EMAIL_MESSAGE)
                    : INVALID_EMAIL_MESSAGE;
            String mobileNumberCheckResult = mobileNumber.matches(MOBILE_NUMBER_REGEX)
                    ? (!dao.checkMobileNumber(mobileNumber) ? VALID : NOT_UNIQUE_MOBILE_NUMBER_MESSAGE)
                    : INVALID_MOBILE_NUMBER_MESSAGE;

            dataCheckResult.put(LOGIN, loginCheckResult);
            dataCheckResult.put(PASSWORD, passwordCheckResult);
            dataCheckResult.put(EMAIL, emailCheckResult);
            dataCheckResult.put(MOBILE_NUMBER, mobileNumberCheckResult);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        return dataCheckResult;
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) throws ServiceException {
        UserDao dao = daoProvider.getUserDao();
        Optional<User> user = Optional.empty();
        try {
            if (login.matches(LOGIN_REGEX) && password.matches(PASSPORT_REGEX)) {
                String passwordHash = PasswordEncryptor.encrypt(password);
                user = dao.findUserByLoginAndPassword(login, password);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public void registerNewUser(RegistrationData data, String password) throws ServiceException {
        User user = new User(data.getLogin(), data.getEmail(), data.getMobileNumber(), LocalDateTime.now());
        UserDao dao = daoProvider.getUserDao();
        try {
            String passwordHash = PasswordEncryptor.encrypt(password);
            dao.addNewUser(user, passwordHash);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}