package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static com.kotov.restaurant.model.dao.ColumnName.USER_STATUS;

public class UserDaoImpl implements UserDao1 {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_USER_BY_LOGIN = "SELECT login FROM users WHERE LOGIN=?";
    private static final String FIND_USER_BY_EMAIL = "SELECT email_address FROM users WHERE email_address=?";
    private static final String FIND_USER_BY_MOBILE_NUMBER = "SELECT mobile_number FROM users WHERE mobile_number=?";
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id" +
            " WHERE login=? AND password=?";
    private static final String FIND_ALL_USER = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id";
    private static final String INSERT_NEW_USER = "INSERT INTO users" +
            " (login, password, email_address, mobile_number, registered, role_id, status_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public User findEntityById(long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAllEntities() throws DaoException {                                       // add address to user
        try (Connection connection = connection_pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_USER)) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getLong(USER_ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setEmail(resultSet.getString(EMAIL_ADDRESS));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setMiddleName(resultSet.getString(MIDDLE_NAME));
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
    public void deleteEntities(List<Long> idList) throws DaoException {
        throw new UnsupportedOperationException();
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
                user.setUserId(resultSet.getLong(USER_ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setEmail(resultSet.getString(EMAIL_ADDRESS));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setMiddleName(resultSet.getString(MIDDLE_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setMobileNumber(resultSet.getString(MOBILE_NUMBER));
                user.setRegistered(LocalDateTime.parse(resultSet.getString(REGISTERED), FORMATTER));
                user.setRole(User.Role.valueOf(resultSet.getString(USER_ROLE).toUpperCase()));
                user.setStatus(User.Status.valueOf(resultSet.getString(USER_STATUS).toUpperCase()));
                userOptional = Optional.of(user);
                logger.log(Level.DEBUG, "findUserByLoginAndPassword method was completed successfully. Result: " + user);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find user from database. Database access error:", e);
            throw new DaoException("Impossible to find user from database. Database access error:", e);
        }
        return userOptional;
    }
}