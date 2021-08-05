package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Cart;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static com.kotov.restaurant.model.dao.ColumnName.USER_STATUS;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_USER_BY_LOGIN = "SELECT id FROM users WHERE login=?";
    private static final String FIND_USER_BY_EMAIL = "SELECT id FROM users WHERE email_address=?";
    private static final String FIND_USER_BY_MOBILE_NUMBER = "SELECT id FROM users WHERE mobile_number=?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id" +
            " WHERE login=? AND password=?";
    private static final String FIND_ALL_USER = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id";
    private static final String FIND_ADDRESS_BY_USER_ID = "SELECT id, city, street, building, block, flat, entrance, floor, intercom_code" +
            " FROM address WHERE user_id=?";
    private static final String FIND_MEALS_FOR_USER_IN_CART = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active, quantity FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id" +
            " JOIN carts ON meal_id=meals.id and user_id=?";
    private static final String INSERT_NEW_USER = "INSERT INTO users" +
            " (login, password, email_address, mobile_number, registered, role_id, status_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_STATUS_BY_USER_ID = "UPDATE users SET status_id=? WHERE id=?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE users.id=?";

    @Override
    public Optional<User> findEntityById(long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public List<User> findAllEntities() throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_USER)) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(USER_ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setEmail(resultSet.getString(EMAIL_ADDRESS));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setPatronymic(resultSet.getString(MIDDLE_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setMobileNumber(resultSet.getString(MOBILE_NUMBER));
                user.setRegistered(LocalDateTime.parse(resultSet.getString(REGISTERED), FORMATTER));
                user.setRole(User.Role.valueOf(resultSet.getString(USER_ROLE).toUpperCase()));
                user.setStatus(User.Status.valueOf(resultSet.getString(USER_STATUS).toUpperCase()));
                users.add(user);
            }
            logger.log(Level.DEBUG, "findAllEntities method was completed successfully. Users: " + users);
            return users;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find all meals. Database access error:", e);
            throw new DaoException("Impossible to find all meals. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(User user) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteEntityById(long id) throws DaoException {
        return false;
    }

    @Override
    public long insertNewEntity(User user, String passwordHash) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, user.getLogin());
            statement.setString(SECOND_PARAM_INDEX, passwordHash);
            statement.setString(THIRD_PARAM_INDEX, user.getEmail());
            statement.setString(FOURTH_PARAM_INDEX, user.getMobileNumber());
            statement.setTimestamp(FIFTH_PARAM_INDEX, Timestamp.valueOf(user.getRegistered()));
            statement.setInt(SIXTH_PARAM_INDEX, user.getRole().ordinal() + 1);
            statement.setInt(SEVENTH_PARAM_INDEX, user.getStatus().ordinal() + 1);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long key = 0;
            if (resultSet.next()) {
                key = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.DEBUG, "insertNewEntity method was completed successfully. User " + user + " was added");
            return key;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert user into database. Database access error:", e);
            throw new DaoException("Impossible to insert user into database. Database access error:", e);
        }
    }

    @Override
    public void updateUserStatusesById(User.Status status, List<Long> userIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_STATUS_BY_USER_ID)) {
            for (Long userId : userIdList) {
                statement.setInt(FIRST_PARAM_INDEX, status.ordinal() + 1);
                statement.setLong(SECOND_PARAM_INDEX, userId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "updateMealStatuses method was completed successfully.");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update meal statuses. Database access error:", e);
            throw new DaoException("Impossible to update meal statuses. Database access error:", e);
        }
    }

    @Override
    public Cart findUserMealsInCart(long userId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEALS_FOR_USER_IN_CART)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            ResultSet resultSet = statement.executeQuery();
            Cart cart = new Cart();
            while (resultSet.next()) {
                Meal meal = new Meal();
                meal.setId(resultSet.getLong(MEAL_ID));
                meal.setTitle(resultSet.getString(MEAL_TITLE));
                meal.setImage(encodeBlob(resultSet.getBlob(MEAL_IMAGE)));
                meal.setType(Meal.Type.valueOf(resultSet.getString(MEAL_TYPES_TYPE).toUpperCase()));
                meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
                meal.setRecipe(resultSet.getString(MEAL_RECIPE));
                meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
                meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
                Integer quantity = resultSet.getInt(MEAL_QUANTITY);
                if (cart.containsKey(meal)) {
                    Integer generalQuantity = cart.get(meal) + quantity;
                    cart.getMeals().replace(meal, generalQuantity);
                } else {
                    cart.put(meal, quantity);
                }
            }
            return cart;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals in cart for user from database. Database access error:", e);
            throw new DaoException("Impossible to find meals in cart for user from database. Database access error:", e);
        }
    }

    private String encodeBlob(Blob image) throws DaoException {                     // to util package
        try {
            byte[] imageBytes = image.getBinaryStream().readAllBytes();
            byte[] encodeBase64 = Base64.getEncoder().encode(imageBytes);
            String base64DataString = new String(encodeBase64, StandardCharsets.UTF_8);
            String src = "data:image/jpeg;base64," + base64DataString;
            return src;
        } catch (SQLException e) {
            throw new DaoException("Image InputStream cannot be received. Error accessing BLOB value:", e);
        } catch (IOException e) {
            throw new DaoException("Image bytes cannot be read from InputStream:", e);
        }
    }

    @Override
    public void deleteEntitiesById(List<Long> idList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            for (Long userId : idList) {
                statement.setLong(FIRST_PARAM_INDEX, userId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "deleteEntities method was completed successfully. Users with id: " + idList + " were deleted");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete users from database. Database access error:", e);
            throw new DaoException("Impossible to delete users from database. Database access error:", e);
        }
    }

    @Override
    public boolean isLoginExist(String login) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
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
        try (Connection connection = connection_pool.getConnection();
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
        try (Connection connection = connection_pool.getConnection();
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
    public Optional<User> findUserByLoginAndPassword(String login, String passwordHah) throws DaoException {
        Optional<User> userOptional = Optional.empty();
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            statement.setString(FIRST_PARAM_INDEX, login);
            statement.setString(SECOND_PARAM_INDEX, passwordHah);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(USER_ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setEmail(resultSet.getString(EMAIL_ADDRESS));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setPatronymic(resultSet.getString(MIDDLE_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setMobileNumber(resultSet.getString(MOBILE_NUMBER));
                user.setRegistered(LocalDateTime.parse(resultSet.getString(REGISTERED), FORMATTER));
                user.setRole(User.Role.valueOf(resultSet.getString(USER_ROLE).toUpperCase()));
                user.setStatus(User.Status.valueOf(resultSet.getString(USER_STATUS).toUpperCase()));
                userOptional = Optional.of(user);
                logger.log(Level.DEBUG, "findUserByLoginAndPassword method was completed successfully. Result: " + user);
            }
            return userOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find user from database. Database access error:", e);
            throw new DaoException("Impossible to find user from database. Database access error:", e);
        }
    }

    @Override
    public Optional<Address> findAddressByUserId(long userId) throws DaoException {
        Optional<Address> addressOptional = Optional.empty();
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ADDRESS_BY_USER_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = new Address();
                address.setId(resultSet.getLong(ADDRESS_ID));
                address.setCity(resultSet.getString(CITY));
                address.setStreet(resultSet.getString(STREET));
                address.setBuilding(resultSet.getInt(BUILDING));
                address.setBlock(resultSet.getString(BLOCK));
                address.setFlat(resultSet.getInt(FLAT));
                address.setEntrance(resultSet.getInt(ENTRANCE));
                address.setFloor(resultSet.getInt(FLOOR));
                address.setIntercomCode(resultSet.getInt(INTERCOM_CODE));
                addressOptional = Optional.of(address);
                logger.log(Level.DEBUG, "findAddressByUserId method was completed successfully. Result: " + address);
            }
            return addressOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find address by user id from database. Database access error:", e);
            throw new DaoException("Impossible to find address by user id from database. Database access error:", e);
        }
    }
}