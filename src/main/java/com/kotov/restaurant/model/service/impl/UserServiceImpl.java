package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.validator.AddressValidator;
import com.kotov.restaurant.util.PasswordEncryptor;
import com.kotov.restaurant.validator.UserValidator;
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
            return optionalUser;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user by login and password:", e);
            throw new ServiceException("Impossible to find user by login and password:", e);
        }
    }

    @Override
    public List<User> findAllUsers() throws ServiceException {
        try {
            List<User> users = userDao.findAllEntities();
            for (User user : users) {
                List<Address> addresses = userDao.findUserAddresses(user.getId());
                user.setAddresses(addresses);
            }
            return users;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find all users: ", e);
            throw new ServiceException("Impossible to find all users: ", e);
        }
    }

    @Override
    public Optional<User> findUserById(long id) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findEntityById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAddresses(userDao.findUserAddresses(id));
            }
            return optionalUser;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user with id " + id, e);
            throw new ServiceException("Impossible to find user with id " + id, e);
        }
    }

    @Override
    public List<Address> findUserAddresses(long userId) throws ServiceException {
        try {
            return userDao.findUserAddresses(userId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user addresses:", e);
            throw new ServiceException("Impossible to find user addresses:", e);
        }
    }

    @Override
    public boolean updateUserStatusesById(User.Status status, String[] userIdArray) throws ServiceException {
        boolean result = false;
        if (userIdArray != null) {
            try {
                List<Long> userIdList = convertArrayToList(userIdArray);
                result = userDao.updateUserStatusesById(status, userIdList);
                logger.log(Level.DEBUG, "updateUserStatuses service method is completed successfully. Result is: " + result);
            } catch (DaoException e) {
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
                result = userDao.deleteEntitiesById(userIdList);
                logger.log(Level.INFO, "removeUsers service method is completed successfully. Result is: " + result);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Impossible to delete users:", e);
                throw new ServiceException("Impossible to delete users:", e);
            }
        }
        return result;
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
                    ? (!userDao.isLoginExist(login) ? TRUE : NOT_UNIQUE_LOGIN_RESULT)
                    : INVALID_LOGIN_RESULT;
            String passwordCheckResult = UserValidator.isPasswordValid(password)
                    ? (password.equals(confirmPassword) ? TRUE : PASSWORD_MISMATCH)
                    : INVALID_PASSPORT_RESULT;
            String emailCheckResult = UserValidator.isEmailValid(email)
                    ? (!userDao.isEmailExist(email) ? TRUE : NOT_UNIQUE_EMAIL_RESULT)
                    : INVALID_EMAIL_RESULT;
            String mobileNumberCheckResult = UserValidator.isMobileNumberValid(mobileNumber)
                    ? (!userDao.isMobileNumberExist(mobileNumber) ? TRUE : NOT_UNIQUE_MOBILE_NUMBER_RESULT)
                    : INVALID_MOBILE_NUMBER_RESULT;

            result = parseBoolean(loginCheckResult) && parseBoolean(passwordCheckResult)
                    && parseBoolean(emailCheckResult) && parseBoolean(mobileNumberCheckResult);

            if (result) {
                User.Role role = roleStr != null ? User.Role.valueOf(roleStr.toUpperCase()) : User.Role.CLIENT;
                User user = new User(login, email, mobileNumber, LocalDateTime.now(), role);
                String passwordHash = PasswordEncryptor.encrypt(password);
                userDao.insertNewEntity(user, passwordHash);
            } else {
                dataCheckResult.remove(USER_ROLE, roleStr);
                dataCheckResult.remove(CONFIRM_PASSWORD, confirmPassword);
                dataCheckResult.replace(LOGIN, loginCheckResult);
                dataCheckResult.replace(PASSWORD, passwordCheckResult);
                dataCheckResult.replace(EMAIL, emailCheckResult);
                dataCheckResult.replace(MOBILE_NUMBER, mobileNumberCheckResult);
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to create new user: ", e);
            throw new ServiceException("Impossible to create new user: ", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Impossible to create new user. This enum type has no constant with the specified name: " + roleStr, e);
            result = false;
        }
        return result;
    }

    @Override
    public Map<Meal, Integer> findMealsInCartByUserId(long userId) throws ServiceException {
        try {
            return userDao.findMealsInCartByUserId(userId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find meals in cart for user: ", e);
            throw new ServiceException("Impossible to find meals in cart for user: ", e);
        }
    }

    @Override
    public boolean deleteMealsFromCartByUserId(long userId, String[] mealIdArray) throws ServiceException {
        List<Long> mealIdList = convertArrayToList(mealIdArray);
        try {
            return userDao.deleteMealsFromCartByUserId(userId, mealIdList);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to delete meals from cart for user with id " + userId, e);
            throw new ServiceException("Impossible to delete meals from cart for user with id " + userId, e);
        }
    }

    private List<Long> convertArrayToList(String[] idArray) {
        List<Long> idList = new ArrayList<>();
        try {
            if (idArray != null) {
                for (String idStr : idArray) {
                    idList.add(Long.parseLong(idStr));
                }
            }
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARN, "Variable idStr does not contain a parsable long:", e);
            idList.clear();
        }
        return idList;
    }

    @Override
    public boolean insertUserAddress(long userId, Map<String, String> dataCheckResult) throws ServiceException {
        String city = dataCheckResult.get(CITY);
        String street = dataCheckResult.get(STREET);
        String building = dataCheckResult.get(BUILDING);
        String block = dataCheckResult.get(BUILDING_BLOCK);
        String flat = dataCheckResult.get(FLAT);
        String entrance = dataCheckResult.get(ENTRANCE);
        String floor = dataCheckResult.get(FLOOR);
        String intercomCode = dataCheckResult.get(INTERCOM_CODE);

        String cityCheckResult = AddressValidator.isCityValid(city) ? TRUE : INVALID_CITY;
        String streetCheckResult = AddressValidator.isStreetValid(street) ? TRUE : INVALID_STREET;
        String buildingCheckResult = AddressValidator.isBuildingValid(building) ? TRUE : INVALID_BUILDING;
        String blockCheckResult = AddressValidator.isBlockValid(block) ? TRUE : INVALID_BUILDING_BLOCK;
        String flatCheckResult = AddressValidator.isFlatValid(flat) ? TRUE : INVALID_FLAT;
        String entranceCheckResult = AddressValidator.isEntranceValid(entrance) ? TRUE : INVALID_ENTRANCE;
        String floorCheckResult = AddressValidator.isFloorValid(floor) ? TRUE : INVALID_FLOOR;
        String intercomCodeCheckResult = AddressValidator.isIntercomCodeValid(intercomCode) ? TRUE : INVALID_INTERCOM_CODE;

        boolean optionalFieldsResult = parseBoolean(blockCheckResult) && parseBoolean(flatCheckResult) && parseBoolean(entranceCheckResult);
        if (optionalFieldsResult) {
            floorCheckResult = parseBoolean(floorCheckResult) && (floor.isEmpty() || !flat.isEmpty() || !entrance.isEmpty())
                    ? TRUE : INVALID_FLOOR;
            intercomCodeCheckResult = parseBoolean(intercomCodeCheckResult) && (intercomCode.isEmpty() || !flat.isEmpty() || !entrance.isEmpty())
                    ? TRUE : INVALID_INTERCOM_CODE;
            optionalFieldsResult = parseBoolean(floorCheckResult) && parseBoolean(intercomCodeCheckResult);
        }

        boolean result = parseBoolean(cityCheckResult) && parseBoolean(streetCheckResult) && parseBoolean(buildingCheckResult) && optionalFieldsResult;

        if (result) {
            Address.City enumCity = Address.City.valueOf(city);
            Address address = new Address(enumCity, street, Integer.parseInt(building));
            if (!block.isEmpty()) {
                address.setBlock(block);
            }
            if (!flat.isEmpty()) {
                address.setFloor(Integer.parseInt(flat));
            }
            if (!entrance.isEmpty()) {
                address.setEntrance(Integer.parseInt(entrance));
            }
            if (!floor.isEmpty()) {
                address.setFloor(Integer.parseInt(floor));
            }
            if (!intercomCode.isEmpty()) {
                address.setIntercomCode(intercomCode);
            }
            try {
                result = userDao.insertUserAddress(userId, address);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Impossible to add address to user with id " + userId, e);
                throw new ServiceException("Impossible to add address to user with id " + userId, e);
            }
        } else {
            dataCheckResult.replace(CITY, cityCheckResult);
            dataCheckResult.replace(STREET, streetCheckResult);
            dataCheckResult.replace(BUILDING, buildingCheckResult);
            dataCheckResult.replace(BUILDING_BLOCK, blockCheckResult);
            dataCheckResult.replace(FLAT, flatCheckResult);
            dataCheckResult.replace(ENTRANCE, entranceCheckResult);
            dataCheckResult.replace(FLOOR, floorCheckResult);
            dataCheckResult.replace(INTERCOM_CODE, intercomCodeCheckResult);
            logger.log(Level.DEBUG, "Address data is invalid for user with id " + userId);
        }
        return result;
    }

    @Override
    public boolean updateFirstNameById(long userId, String firstName) throws ServiceException {
        try {
            return UserValidator.isNameValid(firstName) && userDao.updateUserFirstName(userId, firstName);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user first name:", e);
            throw new ServiceException("Impossible to change user first name:", e);
        }
    }

    @Override
    public boolean updatePatronymicById(long userId, String patronymic) throws ServiceException {
        try {
            System.out.println(UserValidator.isNameValid(patronymic));
            return UserValidator.isNameValid(patronymic) && userDao.updateUserPatronymic(userId, patronymic);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user patronymic:", e);
            throw new ServiceException("Impossible to change user patronymic:", e);
        }
    }

    @Override
    public boolean updateLastNameById(long userId, String lastName) throws ServiceException {
        try {
            return UserValidator.isNameValid(lastName) && userDao.updateUserLastName(userId, lastName);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user last name:", e);
            throw new ServiceException("Impossible to change user last name:", e);
        }
    }

    @Override
    public boolean updateMobileNumberById(long userId, Map<String, String> dataCheckResult) throws ServiceException {
        String mobileNumber = dataCheckResult.get(MOBILE_NUMBER);
        try {
            String mobileNumberCheckResult = UserValidator.isMobileNumberValid(mobileNumber)
                    ? (!userDao.isMobileNumberExist(mobileNumber) ? TRUE : NOT_UNIQUE_MESSAGE)
                    : INVALID_MESSAGE;
            boolean result = parseBoolean(mobileNumberCheckResult);
            if (result) {
                result = userDao.updateUserMobileNumber(userId, mobileNumber);
            } else {
                dataCheckResult.replace(MOBILE_NUMBER, mobileNumberCheckResult);
            }
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user mobile number:", e);
            throw new ServiceException("Impossible to change user mobile number:", e);
        }
    }

    @Override
    public boolean updateEmailById(long userId, Map<String, String> dataCheckResult) throws ServiceException {
        String email = dataCheckResult.get(EMAIL);
        try {
            String emailCheckResult = UserValidator.isEmailValid(email)
                    ? (!userDao.isEmailExist(email) ? TRUE : NOT_UNIQUE_MESSAGE)
                    : INVALID_MESSAGE;
            boolean result = parseBoolean(emailCheckResult);
            if (result) {
                result = userDao.updateUserEmail(userId, email);
            } else {
                dataCheckResult.replace(EMAIL, emailCheckResult);
            }
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user email:", e);
            throw new ServiceException("Impossible to change user email:", e);
        }
    }

    @Override
    public boolean updateAccountPassword(long userId, Map<String, String> dataCheckResult) throws ServiceException {
        String oldPassword = dataCheckResult.get(OLD_PASSWORD);
        String oldPasswordHash = PasswordEncryptor.encrypt(oldPassword);
        try {
            boolean result = UserValidator.isPasswordValid(oldPassword) && userDao.isUserExist(userId, oldPasswordHash);
            if (result) {
                String newPassword = dataCheckResult.get(NEW_PASSWORD);
                String confirmPassword = dataCheckResult.get(CONFIRM_PASSWORD);
                String passwordCheckResult = UserValidator.isPasswordValid(newPassword)
                        ? (newPassword.equals(confirmPassword) ? TRUE : PASSWORD_MISMATCH)
                        : INVALID_PASSPORT;
                result = parseBoolean(passwordCheckResult);
                if (result) {
                    String newPasswordHash = PasswordEncryptor.encrypt(newPassword);
                    result = userDao.updateUserPassword(userId, newPasswordHash);
                } else {
                    dataCheckResult.clear();
                    dataCheckResult.put(NEW_PASSWORD, passwordCheckResult);
                    logger.log(Level.DEBUG, "Invalid password or mismatching password was entered");
                }
            } else {
                dataCheckResult.clear();
                dataCheckResult.put(OLD_PASSWORD, INCORRECT_PASSWORD);
                logger.log(Level.DEBUG, "Incorrect password was entered");
            }
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user password:", e);
            throw new ServiceException("Impossible to change user password:", e);
        }
    }
}