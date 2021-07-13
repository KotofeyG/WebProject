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
import java.util.Optional;

import static com.kotov.restaurant.command.ParamName.*;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String CHECK_LOGIN = "SELECT login FROM users WHERE login=?";
    private static final String CHECK_PASSWORD = "SELECT password FROM users WHERE password=?";
    private static final String CHECK_EMAIL = "SELECT email_address FROM users WHERE email_address=?";
    private static final String CHECK_MOBILE_NUMBER = "SELECT mobile_number FROM users WHERE mobile_number=?";
    private static final String ADD_NEW_USER = "INSERT INTO users" +
            " (login, password, email_address, mobile_number, registered, role_id, status_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

    private UserDaoImpl() {
    }

    private static class UserDaoImplHolder {
        private static final UserDaoImpl INSTANCE = new UserDaoImpl();
    }

    public static UserDaoImpl getInstance() {
        return UserDaoImplHolder.INSTANCE;
    }

    @Override
    public boolean checkLogin(String login) throws DaoException {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CHECK_LOGIN)) {
            statement.setString(FIRST_PARAM_INDEX, login);
            ResultSet set = statement.executeQuery();
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public boolean checkEmail(String email) throws DaoException {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CHECK_EMAIL)) {
            statement.setString(FIRST_PARAM_INDEX, email);
            ResultSet set = statement.executeQuery();
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public boolean checkMobileNumber(String mobileNumber) throws DaoException {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CHECK_MOBILE_NUMBER)) {
            statement.setString(FIRST_PARAM_INDEX, mobileNumber);
            ResultSet set = statement.executeQuery();
            return set.isBeforeFirst();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<User> getUserByPassword(String passwordHah) throws DaoException {
        Connection connection = CONNECTION_POOL.getConnection();
        Optional<User> userOptional = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(CHECK_PASSWORD)) {
            statement.setString(FIRST_PARAM_INDEX, passwordHah);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                User user = new User();
                user.setUserId(set.getLong(ID));
                user.setLogin(set.getString(LOGIN));
                user.setEmail(set.getString(EMAIL));
                user.setFirstName(set.getString(FIRST_NAME));
                user.setMiddleName(set.getString(MIDDLE_NAME));
                user.setLastName(set.getString(LAST_NAME));
                user.setMobileNumber(set.getString(MOBILE_NUMBER));
                user.setRegistered(LocalDateTime.parse(set.getString(REGISTERED), FORMATTER));
                //to do
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return userOptional;
    }

    @Override
    public void addNewUser(User user, String passwordHash) throws DaoException {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(ADD_NEW_USER)) {
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
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}