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
import com.kotov.restaurant.validator.DiscountCardValidator;
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
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to find user by login and password:", e);
            throw new ServiceException("Impossible to find user by login and password:", e);
        }
        return optionalUser;
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
    public List<Address> findUserAddresses(String userIdStr) throws ServiceException {
        long userId = Long.parseLong(userIdStr);
        return findUserAddresses(userId);
    }

    @Override
    public boolean updateUserStatusesById(String statusStr, String[] userIdArray) throws ServiceException {
        boolean result = false;
        if (userIdArray != null) {
            try {
                List<Long> userIdList = convertArrayToList(userIdArray);
                User.Status status = User.Status.valueOf(statusStr);
                result = userDao.updateUserStatusesById(status, userIdList);
                logger.log(Level.DEBUG, "updateUserStatuses service method is completed successfully. Result is: " + result);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Impossible to update user statuses:", e);
                throw new ServiceException("Impossible to update user statuses:", e);
            } catch (IllegalArgumentException e) {
                logger.log(Level.ERROR, "This enum type has no constant with the specified name: " + statusStr);
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
                logger.log(Level.ERROR, "Impossible to remove users:", e);
                throw new ServiceException("Impossible to remove users:", e);
            }
        }
        return result;
    }

    private List<Long> convertArrayToList(String[] idArray) throws ServiceException {
        List<Long> idList = new ArrayList<>();
        try {
            if (idArray != null) {
                for (String idStr : idArray) {
                    idList.add(Long.parseLong(idStr));
                }
            }
            return idList;
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Variable idStr does not contain a parsable long:", e);
            throw new ServiceException("Variable idStr does not contain a parsable long:", e);
        }
    }

    @Override
    public boolean registerNewUser(Map<String, String> dataCheckResult) throws ServiceException {
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

            boolean result = parseBoolean(loginCheckResult) && parseBoolean(passwordCheckResult)
                    && parseBoolean(emailCheckResult) && parseBoolean(mobileNumberCheckResult);

            if (result) {
                User.Role role;
                if (roleStr != null) {
                    role = User.Role.valueOf(roleStr.toUpperCase());
                } else {
                    role = User.Role.CLIENT;
                }
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
            return result;
        } catch (DaoException | IllegalArgumentException e) {
            logger.log(Level.ERROR, "Impossible to create new user: ", e);
            throw new ServiceException("Impossible to create new user: ", e);
        }
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
    public Map<Meal, Integer> findMealsInCartByUserId(String userIdStr) throws ServiceException {
        long userId = Long.parseLong(userIdStr);
        return findMealsInCartByUserId(userId);
    }

    @Override
    public boolean deleteMealsFromCartByUserId(long userId, String[] mealIdArray) throws ServiceException {
        boolean result = false;
        List<Long> mealIdList = convertArrayToList(mealIdArray);
        try {
            result = userDao.deleteMealsFromCartByUserId(userId, mealIdList);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to delete meals from cart for user: ", e);
            throw new ServiceException("Impossible to delete meals from cart for user: ", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "mealIdStr doesn't contain a parsable long: ", e);
        }
        return result;
    }

    @Override
    public boolean addUserAddress(long userId, Map<String, String> dataCheckResult) throws ServiceException {
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
            Address.City enumCity = null;
            for (Address.City next : Address.City.values()) {
                if (next.getRussianName().equals(city)) {
                    enumCity = next;
                }
            }
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
                logger.log(Level.ERROR, "Impossible to add address to user:", e);
                throw new ServiceException("Impossible to add address to user:", e);
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
    public boolean addDiscountCardToUser(long userId, String number) throws ServiceException {
        try {
            return DiscountCardValidator.isCardNumberValid(number)
                    && userDao.isDiscountCardActive(number)
                    && userDao.addDiscountCardToUser(userId, number);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to add discount card to user:", e);
            throw new ServiceException("Impossible to add discount card to user:", e);
        }
    }

    @Override
    public boolean changeUserPersonalData(long userId, Map<String, String> dataCheckResult) throws ServiceException {
        String firstName = dataCheckResult.get(FIRST_NAME);
        String patronymic = dataCheckResult.get(PATRONYMIC);
        String lastName = dataCheckResult.get(LAST_NAME);
        String mobileNumber = dataCheckResult.get(MOBILE_NUMBER);
        String email = dataCheckResult.get(EMAIL);

        try {
            String firstNameCheckResult = UserValidator.isNameValid(firstName) ? TRUE : INVALID_FIRST_NAME;
            String patronymicCheckResult = UserValidator.isNameValid(patronymic) ? TRUE : INVALID_PATRONYMIC;
            String lastNameCheckResult = UserValidator.isNameValid(lastName) ? TRUE : INVALID_LAST_NAME;
            String emailCheckResult = UserValidator.isEmailValid(email)
                    ? (!userDao.isEmailExist(email) ? TRUE : NOT_UNIQUE_EMAIL_RESULT)
                    : INVALID_EMAIL;
            String mobileNumberCheckResult = UserValidator.isMobileNumberValid(mobileNumber)
                    ? (!userDao.isMobileNumberExist(mobileNumber) ? TRUE : NOT_UNIQUE_MOBILE_NUMBER_RESULT)
                    : INVALID_MOBILE_NUMBER;
            boolean result = parseBoolean(firstNameCheckResult)
                    && parseBoolean(patronymicCheckResult)
                    && parseBoolean(lastNameCheckResult)
                    && parseBoolean(emailCheckResult)
                    && parseBoolean(mobileNumberCheckResult);
            if (result) {
                if (!firstName.isEmpty()) {
                    result &= userDao.updateUserFirstName(userId, firstName);
                }
                if (!patronymic.isEmpty()) {
                    result &= userDao.updateUserPatronymic(userId, patronymic);
                }
                if (!lastName.isEmpty()) {
                    result &= userDao.updateUserLastName(userId, lastName);
                }
                if (!mobileNumber.isEmpty()) {
                    result &= userDao.updateUserMobileNumber(userId, mobileNumber);
                }
                if (!email.isEmpty()) {
                    result &= userDao.updateUserEmail(userId, email);
                }
            } else {
                dataCheckResult.replace(FIRST_NAME, firstNameCheckResult);
                dataCheckResult.replace(PATRONYMIC, patronymicCheckResult);
                dataCheckResult.replace(LAST_NAME, lastNameCheckResult);
                dataCheckResult.replace(MOBILE_NUMBER, mobileNumberCheckResult);
                dataCheckResult.replace(EMAIL, emailCheckResult);
                logger.log(Level.DEBUG, "Personal data is invalid for user with id " + userId);
            }
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Impossible to change user personal data:", e);
            throw new ServiceException("Impossible to change user personal data:", e);
        }
    }

    @Override
    public boolean changeAccountPassword(long userId, Map<String, String> dataCheckResult) throws ServiceException {
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
            logger.log(Level.ERROR, "Impossible to change user personal data:", e);
            throw new ServiceException("Impossible to change user personal data:", e);
        }
    }
}