package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.UserDao;
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

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String CHECK_LOGIN = "SELECT login FROM users WHERE LOGIN=?";
    private static final String CHECK_EMAIL = "SELECT email_address FROM users WHERE email_address=?";
    private static final String CHECK_MOBILE_NUMBER = "SELECT mobile_number FROM users WHERE mobile_number=?";
    private static final String ADD_NEW_USER = "INSERT INTO users" +
            " (login, password, email_address, mobile_number, registered, role_id, status_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_USER_BY_LOGIN_PASSWORD = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id" +
            " WHERE login=? AND password=?";
    private static final String FIND_ALL_USER = "SELECT users.id, login, password, email_address" +
            ", first_name, middle_name, last_name, mobile_number, registered, role, status FROM users" +
            " JOIN roles ON role_id=roles.id" +
            " JOIN user_statuses ON status_id=user_statuses.id";


    @Override
    public boolean checkLogin(String login) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_LOGIN)) {
            statement.setString(FIRST_PARAM_INDEX, login);
            ResultSet set = statement.executeQuery();
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean checkEmail(String email) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_EMAIL)) {
            statement.setString(FIRST_PARAM_INDEX, email);
            ResultSet set = statement.executeQuery();
            System.out.println(set.isBeforeFirst());
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean checkMobileNumber(String mobileNumber) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_MOBILE_NUMBER)) {
            statement.setString(FIRST_PARAM_INDEX, mobileNumber);
            ResultSet set = statement.executeQuery();
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String passwordHah) throws DaoException {
        Optional<User> userOptional = Optional.empty();
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN_PASSWORD)) {
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
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userOptional;
    }

    @Override
    public List<User> findAllUsers() throws DaoException {
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
             return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void addNewUser(User user, String passwordHash) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_USER)) {
            statement.setString(FIRST_PARAM_INDEX, user.getLogin());
            statement.setString(SECOND_PARAM_INDEX, passwordHash);
            statement.setString(THIRD_PARAM_INDEX, user.getEmail());
            statement.setString(FOURTH_PARAM_INDEX, user.getMobileNumber());
            statement.setTimestamp(FIFTH_PARAM_INDEX, Timestamp.valueOf(user.getRegistered()));
            statement.setInt(SIXTH_PARAM_INDEX, user.getRole().ordinal() + 1);
            statement.setInt(SEVENTH_PARAM_INDEX, user.getStatus().ordinal() + 1);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "New user added");
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}