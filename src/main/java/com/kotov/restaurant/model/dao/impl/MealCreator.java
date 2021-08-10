package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.util.ImageEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static com.kotov.restaurant.model.dao.ColumnName.MEAL_ACTIVE;

class MealCreator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private MealCreator() {
    }

    static Meal create(ResultSet resultSet) throws SQLException, DaoException {
        Meal meal = new Meal();
        meal.setId(resultSet.getLong(MEAL_ID));
        meal.setTitle(resultSet.getString(MEAL_TITLE));
        meal.setImage(ImageEncoder.encodeBlob(resultSet.getBlob(MEAL_IMAGE)));
        meal.setType(Meal.Type.valueOf(resultSet.getString(MEAL_TYPES_TYPE).toUpperCase()));
        meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
        meal.setRecipe(resultSet.getString(MEAL_RECIPE));
        meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
        meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
        return meal;
    }
}