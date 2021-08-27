package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 *  The {@link UserDaoImpl} class provides access to
 *  users table in the database
 */
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String FIND_USER_BY_ID = "SELECT users.id, login, password, email_address" +
            ", first_name, patronymic, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id" +
            " WHERE users.id=?";
    private static final String FIND_USER_BY_LOGIN = "SELECT id FROM users WHERE login=?";
    private static final String FIND_USER_BY_EMAIL = "SELECT id FROM users WHERE email_address=?";
    private static final String FIND_USER_BY_MOBILE_NUMBER = "SELECT id FROM users WHERE mobile_number=?";
    private static final String FIND_USER_BY_ID_AND_PASSWORD = "SELECT id FROM users WHERE id=? AND password=?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT users.id, login, password, email_address" +
            ", first_name, patronymic, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id" +
            " WHERE login=? AND password=?";
    private static final String FIND_ALL_USER = "SELECT users.id, login, password, email_address" +
            ", first_name, patronymic, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id";
    private static final String FIND_USER_IN_ANY_ORDER = "SELECT id FROM orders WHERE user_id=? LIMIT 1";
    private static final String FIND_USER_STATUS_BY_ID = "SELECT status FROM users JOIN user_statuses ON user_statuses.id=status_id WHERE users.id=?";
    private static final String FIND_ADDRESS_BY_ID = "SELECT address.id, city, street, building, block, flat, entrance, floor, intercom_code FROM address" +
            " JOIN city_names ON city_names.id=city_id" +
            " WHERE address.id=?";
    private static final String FIND_ADDRESS_BY_USER_ID = "SELECT address.id, city, street, building, block, flat, entrance, floor, intercom_code FROM address" +
            " JOIN city_names ON city_names.id=city_id" +
            " WHERE address.user_id=?";
    private static final String FIND_MEALS_FOR_USER_IN_CART = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active, quantity FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id" +
            " JOIN carts ON meal_id=meals.id and user_id=?";
    private static final String INSERT_NEW_USER = "INSERT INTO users (login, password, email_address, mobile_number, registered, role_id, status_id)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_ADDRESS = "INSERT INTO address (city_id, street, building, block, flat, entrance, floor, intercom_code)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_ADDRESS_FOR_USER = "INSERT INTO address (city_id, street, building, block, flat, entrance, floor, intercom_code, user_id)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_STATUS_BY_USER_ID = "UPDATE users SET status_id=? WHERE id=?";
    private static final String UPDATE_USER_FIRST_NAME = "UPDATE users SET first_name=? WHERE id=?";
    private static final String UPDATE_USER_PATRONYMIC = "UPDATE users SET patronymic=? WHERE id=?";
    private static final String UPDATE_USER_LAST_NAME = "UPDATE users SET last_name=? WHERE id=?";
    private static final String UPDATE_USER_MOBILE_NUMBER = "UPDATE users SET mobile_number=? WHERE id=?";
    private static final String UPDATE_USER_EMAIL = "UPDATE users SET email_address=? WHERE id=?";
    private static final String UPDATE_USER_PASSWORD = "UPDATE users SET password=? WHERE id=?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE users.id=?";
    private static final String DELETE_MEAL_FROM_CART = "DELETE FROM carts WHERE user_id=? AND meal_id=?";
    private static final String DELETE_ADDRESS_FROM_USER = "UPDATE address SET user_id=NULL WHERE id=? AND user_id=?";

    @Override
    public Optional<User> findEntityById(long id) throws DaoException {
        Optional<User> userOptional = Optional.empty();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = UserCreator.create(resultSet);
                userOptional = Optional.of(user);
            }
            logger.log(Level.DEBUG, "findEntityById method was completed successfully."
                    + ((userOptional.isPresent()) ? " User with id " + id + " was found" : " User with id " + id + " don't exist"));
            return userOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find user by id. Database access error:", e);
            throw new DaoException("Impossible to find user by id. Database access error:", e);
        }
    }

    @Override
    public List<User> findAllEntities() throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_USER)) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = UserCreator.create(resultSet);
                users.add(user);
            }
            logger.log(Level.DEBUG, "findAllEntities method was completed successfully. " + users.size() + " were found");
            return users;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find users. Database access error:", e);
            throw new DaoException("Impossible to find users. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(User user) {
        throw new UnsupportedOperationException("insertNewEntity(User user) method is not supported");
    }

    @Override
    public boolean deleteEntityById(long id) {
        throw new UnsupportedOperationException("deleteEntityById(long id) method is not supported");
    }

    @Override
    public long insertNewEntity(User user, String passwordHash) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_USER, RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, user.getLogin());
            statement.setString(SECOND_PARAM_INDEX, passwordHash);
            statement.setString(THIRD_PARAM_INDEX, user.getEmail());
            statement.setString(FOURTH_PARAM_INDEX, user.getMobileNumber());
            statement.setTimestamp(FIFTH_PARAM_INDEX, Timestamp.valueOf(user.getRegistered()));
            statement.setInt(SIXTH_PARAM_INDEX, user.getRole().ordinal() + 1);
            statement.setInt(SEVENTH_PARAM_INDEX, user.getStatus().ordinal() + 1);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long userId = 0;
            if (resultSet.next()) {
                userId = resultSet.getLong(FIRST_PARAM_INDEX);
                logger.log(Level.INFO, "insertNewEntity method was completed successfully. User with id " + userId + " was added");
            }
            return userId;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert new user into database. Database access error:", e);
            throw new DaoException("Impossible to insert new user into database. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserFirstName(long userId, String firstName) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_FIRST_NAME)) {
            statement.setString(FIRST_PARAM_INDEX, firstName);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, "Result of user first name update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user first name. Database access error:", e);
            throw new DaoException("Impossible to update user first name. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserPatronymic(long userId, String patronymic) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_PATRONYMIC)) {
            statement.setString(FIRST_PARAM_INDEX, patronymic);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, "Result of user patronymic update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user patronymic. Database access error:", e);
            throw new DaoException("Impossible to update user patronymic. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserLastName(long userId, String lastName) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_LAST_NAME)) {
            statement.setString(FIRST_PARAM_INDEX, lastName);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, "Result of user last name update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user last name. Database access error:", e);
            throw new DaoException("Impossible to update user last name. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserMobileNumber(long userId, String mobileNumber) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_MOBILE_NUMBER)) {
            statement.setString(FIRST_PARAM_INDEX, mobileNumber);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, "Result of user mobile number update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user mobile number. Database access error:", e);
            throw new DaoException("Impossible to update user mobile number. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserEmail(long userId, String email) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_EMAIL)) {
            statement.setString(FIRST_PARAM_INDEX, email);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, "Result of user email update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user email. Database access error:", e);
            throw new DaoException("Impossible to update user email. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserPassword(long userId, String passwordHash) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_PASSWORD)) {
            statement.setString(FIRST_PARAM_INDEX, passwordHash);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, "Result of user password update for user with id " + userId + " is " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update user password. Database access error:", e);
            throw new DaoException("Impossible to update user password. Database access error:", e);
        }
    }

    @Override
    public boolean updateUserStatusesById(User.Status status, List<Long> userIdList) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_STATUS_BY_USER_ID)) {
            for (Long userId : userIdList) {
                statement.setInt(FIRST_PARAM_INDEX, status.ordinal() + 1);
                statement.setLong(SECOND_PARAM_INDEX, userId);
                statement.addBatch();
            }
            boolean result = statement.executeBatch().length == userIdList.size();
            logger.log(Level.INFO, result ? "updateUserStatusesById method was completed successfully. User statuses with user id list "
                    + userIdList + " were updated to " + status + " statuses" : "User statuses with user id list " + userIdList + " weren't updated to " + status + " statuses");
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update users statuses. Database access error:", e);
            throw new DaoException("Impossible to update users statuses. Database access error:", e);
        }
    }

    @Override
    public Map<Meal, Integer> findMealsInCartByUserId(long userId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEALS_FOR_USER_IN_CART)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            ResultSet resultSet = statement.executeQuery();
            Map<Meal, Integer> userCart = new LinkedHashMap<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                Integer quantity = resultSet.getInt(MEAL_QUANTITY);
                if (userCart.containsKey(meal)) {
                    Integer generalQuantity = userCart.get(meal) + quantity;
                    userCart.replace(meal, generalQuantity);
                } else {
                    userCart.put(meal, quantity);
                }
            }
            logger.log(Level.DEBUG, "findMealsInCartByUserId method was completed successfully. " +
                    (!userCart.isEmpty() ? userCart.size() + " different items were found in user cart with user id " + userId
                            : " User with id " + userId + " doesn't have any items in cart"));
            return userCart;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals in cart for user in database. Database access error:", e);
            throw new DaoException("Impossible to find meals in cart for user in database. Database access error:", e);
        }
    }

    @Override
    public boolean deleteMealsFromCartByUserId(long userId, List<Long> mealIdList) throws DaoException {
        boolean result;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL_FROM_CART)) {
            for (Long mealId : mealIdList) {
                statement.setLong(FIRST_PARAM_INDEX, userId);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            result = statement.executeBatch().length == mealIdList.size();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete meals from cart. Database access error:", e);
            throw new DaoException("Impossible to delete meals from cart. Database access error:", e);
        }
        logger.log(Level.DEBUG, "deleteMealsFromCartByUserId method was completed successfully."
                + (result ? " Meals with id list " + mealIdList + " were deleted from user cart with user id " + userId
                : " Meals with id list " + mealIdList + " weren't deleted from user cart with user id " + userId));
        return result;
    }

    @Override
    public boolean deleteUserAddress(long userId, long addressId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ADDRESS_FROM_USER)) {
            statement.setLong(FIRST_PARAM_INDEX, addressId);
            statement.setLong(SECOND_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.DEBUG, result ? "Address with id " + addressId + " was deleted from user with id " + userId
                     : "Address with id " + addressId + " wasn't deleted from user with id " + userId);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete address from user. Database access error:", e);
            throw new DaoException("Impossible to delete address from user. Database access error:", e);
        }
    }

    @Override
    public boolean deleteEntitiesById(List<Long> idList) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID);
             PreparedStatement checkStatement = connection.prepareStatement(FIND_USER_IN_ANY_ORDER)) {
            for (Long userId : idList) {
                checkStatement.setLong(FIRST_PARAM_INDEX, userId);
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.isBeforeFirst()) {
                    statement.setLong(FIRST_PARAM_INDEX, userId);
                    statement.addBatch();
                }
            }
            boolean result = statement.executeBatch().length > 0;
            logger.log(Level.INFO, result ? "deleteEntitiesById method was completed successfully. Users with id " + idList + " were deleted"
                    : "Users with id " + idList + " weren't deleted");
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete users from database. Database access error:", e);
            throw new DaoException("Impossible to delete users from database. Database access error:", e);
        }
    }

    @Override
    public boolean isLoginExist(String login) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN)) {
            statement.setString(FIRST_PARAM_INDEX, login);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isLoginExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of user login. Database access error:", e);
            throw new DaoException("Impossible to check existence of user login. Database access error:", e);
        }
    }

    @Override
    public boolean isEmailExist(String email) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
            statement.setString(FIRST_PARAM_INDEX, email);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isEmailExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of user email. Database access error:", e);
            throw new DaoException("Impossible to check existence of user email. Database access error:", e);
        }
    }

    @Override
    public boolean isMobileNumberExist(String mobileNumber) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_MOBILE_NUMBER)) {
            statement.setString(FIRST_PARAM_INDEX, mobileNumber);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isMobileNumberExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of user mobile number. Database access error:", e);
            throw new DaoException("Impossible to check existence of user mobile number. Database access error:", e);
        }
    }

    @Override
    public boolean isUserExist(long userId, String passwordHash) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID_AND_PASSWORD)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            statement.setString(SECOND_PARAM_INDEX, passwordHash);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isUserExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of user. Database access error:", e);
            throw new DaoException("Impossible to check existence of user. Database access error:", e);
        }
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String passwordHah) throws DaoException {
        Optional<User> userOptional = Optional.empty();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            statement.setString(FIRST_PARAM_INDEX, login);
            statement.setString(SECOND_PARAM_INDEX, passwordHah);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = UserCreator.create(resultSet);
                userOptional = Optional.of(user);
            }
            logger.log(Level.DEBUG, "findUserByLoginAndPassword method was completed successfully." +
                    (userOptional.map(user -> " User with id " + user.getId() + " was found").orElse(" User with these login and password don't exist")));
            return userOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find user in database. Database access error:", e);
            throw new DaoException("Impossible to find user in database. Database access error:", e);
        }
    }

    @Override
    public User.Status findUserStatusById(long id) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_STATUS_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            User.Status status = null;
            if (resultSet.next()) {
                status = User.Status.valueOf(resultSet.getString(USER_STATUS).toUpperCase());
            }
            logger.log(Level.DEBUG, status != null ? "User status is " + status : "User doesnt have any status");
            return status;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find user status by user id from database. Database access error:", e);
            throw new DaoException("Impossible to find user status by user id from database. Database access error:", e);
        }
    }

    @Override
    public Optional<Address> findAddressById(long addressId) throws DaoException {
        Optional<Address> addressOptional = Optional.empty();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ADDRESS_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, addressId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = AddressCreator.create(resultSet);
                addressOptional = Optional.of(address);
            }
            logger.log(Level.DEBUG, "findAddressById method was completed successfully." +
                    (addressOptional.isPresent() ? " Address with id " + addressId + " was found"
                            : " Address with id " + addressId + " doesn't exist"));
            return addressOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find address by user id from database. Database access error:", e);
            throw new DaoException("Impossible to find address by user id from database. Database access error:", e);
        }
    }

    @Override
    public List<Address> findUserAddresses(long userId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ADDRESS_BY_USER_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Address> addresses = new ArrayList<>();
            while (resultSet.next()) {
                Address address = AddressCreator.create(resultSet);
                addresses.add(address);
            }
            logger.log(Level.DEBUG, "findUserAddresses method was completed successfully. " +
                    (!addresses.isEmpty() ? addresses.size() + " addresses were found for user with id " + userId
                            : "User with id " + userId + " doesn't have any addresses"));
            return addresses;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find addresses by user id from database. Database access error:", e);
            throw new DaoException("Impossible to find addresses by user id from database. Database access error:", e);
        }
    }

    @Override
    public boolean insertUserAddress(long userId, Address address) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ADDRESS_FOR_USER)) {
            statement.setLong(FIRST_PARAM_INDEX, address.getCity().ordinal() + 1);
            statement.setString(SECOND_PARAM_INDEX, address.getStreet());
            statement.setInt(THIRD_PARAM_INDEX, address.getBuilding());
            statement.setString(FOURTH_PARAM_INDEX, address.getBlock());
            statement.setInt(FIFTH_PARAM_INDEX, address.getFlat());
            statement.setInt(SIXTH_PARAM_INDEX, address.getEntrance());
            statement.setInt(SEVENTH_PARAM_INDEX, address.getFloor());
            statement.setString(EIGHT_PARAM_INDEX, address.getIntercomCode());
            statement.setLong(NINE_PARAM_INDEX, userId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, "insertUserAddress method was completed successfully."
                    + (result ? "New address was inserted for user with id " + userId : "New address wasn't inserted for user with id " + userId));
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert new address for user with id " + userId + " into database. Database access error:", e);
            throw new DaoException("Impossible to insert new address for user with id " + userId + " into database. Database access error:", e);
        }
    }

    @Override
    public long insertAddress(Address address) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ADDRESS, RETURN_GENERATED_KEYS)) {
            statement.setLong(FIRST_PARAM_INDEX, address.getCity().ordinal() + 1);
            statement.setString(SECOND_PARAM_INDEX, address.getStreet());
            statement.setInt(THIRD_PARAM_INDEX, address.getBuilding());
            statement.setString(FOURTH_PARAM_INDEX, address.getBlock());
            statement.setInt(FIFTH_PARAM_INDEX, address.getFlat());
            statement.setInt(SIXTH_PARAM_INDEX, address.getEntrance());
            statement.setInt(SEVENTH_PARAM_INDEX, address.getFloor());
            statement.setString(EIGHT_PARAM_INDEX, address.getIntercomCode());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long addressId = 0;
            if (resultSet.next()) {
                addressId = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.INFO, addressId != 0 ? "insertAddress method was completed successfully. Address with id " + addressId + " was inserted"
                    : "Address wasn't inserted into database");
            return addressId;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert new address into database. Database access error:", e);
            throw new DaoException("Impossible to insert new address into database. Database access error:", e);
        }
    }
}