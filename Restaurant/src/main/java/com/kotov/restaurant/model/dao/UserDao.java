package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends BaseDao<User> {

    /**
     * @param login current login
     * @return true if login exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isLoginExist(String login) throws DaoException;

    /**
     * @param email current mobile number
     * @return true if email exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isEmailExist(String email) throws DaoException;

    /**
     * @param mobileNumber current mobile number
     * @return true if mobile number exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isMobileNumberExist(String mobileNumber) throws DaoException;

    /**
     * @param userId current {@link User} id
     * @param passwordHash {@link User}s password
     * @return true if {@link User} exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isUserExist(long userId, String passwordHash) throws DaoException;

    /**
     * @param login {@link User}s login
     * @param passwordHah {@link User}s password
     * @return Optional <{@link User}>
     * @throws DaoException if the request to data base could not be handled
     */
    Optional<User> findUserByLoginAndPassword(String login, String passwordHah)  throws DaoException;

    /**
     * @param id {@link User}s id for which will be find {@link User.Status}
     * @return collection of {@link Address}es
     * @throws DaoException if the request to data base could not be handled
     */
    User.Status findUserStatusById(long id) throws DaoException;

    /**
     * @param addressId {@link Address} id which will be find
     * @return Optional <{@link Address}>
     * @throws DaoException if the request to data base could not be handled
     */
    Optional<Address> findAddressById(long addressId) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be find {@link Address}es
     * @return collection of {@link Address}es
     * @throws DaoException if the request to data base could not be handled
     */
    List<Address> findUserAddresses(long userId) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be find {@link Meal}s in cart
     * @return collection of {@link Meal}s into user cart
     * @throws DaoException if the request to data base could not be handled
     */
    Map<Meal, Integer> findMealsInCartByUserId(long userId) throws DaoException;

    /**
     * @param address {@link Address}
     * @return true if {@link Address} was added into data base, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    long insertAddress(Address address) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be added new address
     * @param address {@link Address}
     * @return true if {@link Address} was added to user personal data, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean insertUserAddress(long userId, Address address) throws DaoException;

    /**
     * @param user current {@link User}
     * @param passwordHash {@link User}s password
     * @return get user id if {@link User} was added to data base, otherwise 0
     * @throws DaoException if the request to data base could not be handled
     */
    long insertNewEntity(User user, String passwordHash) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed first name
     * @param firstName {@link User}s first name
     * @return true if first name was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserFirstName(long userId, String firstName) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed patronymic
     * @param patronymic {@link User}s patronymic
     * @return true if patronymic was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserPatronymic(long userId, String patronymic) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed last name
     * @param lastName {@link User}s last name
     * @return true if last name was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserLastName(long userId, String lastName) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed mobile number
     * @param mobileNumber {@link User}s mobile number
     * @return true if mobile number was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserMobileNumber(long userId, String mobileNumber) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed email
     * @param email {@link User}s email
     * @return true if email was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserEmail(long userId, String email) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be changed password
     * @param passwordHash {@link User}s password
     * @return true if password was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserPassword(long userId, String passwordHash) throws DaoException;

    /**
     * @param userIdList {@link User}s id, which  status should be changed
     * @param status current status
     * @return true if status was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateUserStatusesById(User.Status status, List<Long> userIdList) throws DaoException;

    /**
     * @param userId {@link User}s id, which {@link Meal}s should be deleted from cart
     * @param mealIdList {@link Meal}s id
     * @return true if one of {@link Meal}s were deleted, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean deleteMealsFromCartByUserId(long userId, List<Long> mealIdList) throws DaoException;

    /**
     * @param userId {@link User}s id, which {@link Meal}s should be deleted from cart
     * @param addressId {@link Address} id
     * @return true if {@link Address} was deleted, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean deleteUserAddress(long userId, long addressId) throws DaoException;
}