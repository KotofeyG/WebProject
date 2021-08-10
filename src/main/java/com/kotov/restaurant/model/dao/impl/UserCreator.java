package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static com.kotov.restaurant.model.dao.ColumnName.USER_STATUS;

class UserCreator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private UserCreator() {
    }

    static User create(ResultSet resultSet) throws SQLException {
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
        return user;
    }
}