package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.kotov.restaurant.model.dao.ColumnName.*;

public class MealDaoImpl implements MealDao1 {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_MEAL_BY_TITLE = "SELECT id FROM meals WHERE TITLE=?";
    private static final String FIND_MEAL_BY_ID = "SELECT title, image, type, price, recipe, created, active FROM meals WHERE id=?";
    private static final String FIND_ALL_MEALS = "SELECT id, title, image, type, price, recipe, created, active FROM meals";
    private static final String INSERT_NEW_MEAL = "INSERT INTO meals (title, image, type, price, recipe, created, active) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MEAL_STATUS = "UPDATE meals SET active=? WHERE id=?";
    private static final String DELETE_MEAL = "DELETE FROM meals WHERE meals.id=?";

    @Override
    public Meal findEntityById(long id) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEAL_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            Meal meal = new Meal();
            if (resultSet.next()) {
                meal.setMealId(id);
                meal.setTitle(resultSet.getString(MEAL_TITLE));
//                meal.setImage(resultSet.getBlob(MEAL_IMAGE));
                meal.setType(resultSet.getString(MEAL_TYPE));
                meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
                meal.setRecipe(resultSet.getString(MEAL_RECIPE));
                meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
                meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
                logger.log(Level.DEBUG, "findEntityById method was completed successfully. Meal " + meal);
            }
            return meal;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meal by id. Database access error:", e);
            throw new DaoException("Impossible to find meal by id. Database access error:", e);
        }
    }

    @Override
    public List<Meal> findAllEntities() throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_MEALS)) {
            List<Meal> meals = new ArrayList<>();
            while (resultSet.next()) {
                Meal meal = new Meal();
                meal.setMealId(resultSet.getLong(MEAL_ID));
                meal.setTitle(resultSet.getString(MEAL_TITLE));
//                meal.setImage(resultSet.getBlob(MEAL_IMAGE));
                meal.setType(resultSet.getString(MEAL_TYPE));
                meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
                meal.setRecipe(resultSet.getString(MEAL_RECIPE));
                meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
                meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "findAllEntities method was completed successfully. Meals: " + meals);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find all meals. Database access error:", e);
            throw new DaoException("Impossible to find all meals. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(Meal meal) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_MEAL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, meal.getTitle());
            statement.setBlob(SECOND_PARAM_INDEX, meal.getImage());
            statement.setString(THIRD_PARAM_INDEX, meal.getType());
            statement.setBigDecimal(FOURTH_PARAM_INDEX, meal.getPrice());
            statement.setString(FIFTH_PARAM_INDEX, meal.getRecipe());
            statement.setTimestamp(SIXTH_PARAM_INDEX, Timestamp.valueOf(meal.getCreated()));
            statement.setBoolean(SEVENTH_PARAM_INDEX, meal.isActive());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long key = 0;
            if (resultSet.next()) {
                key = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.DEBUG, "insertNewEntity method was completed successfully. Meal " + meal + " was added");
            return key;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert meal into database. Database access error:", e);
            throw new DaoException("Impossible to insert meal into database. Database access error:", e);
        }
    }

    @Override
    public void deleteEntities(List<Long> idList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL)) {
            for (Long mealId : idList) {
                statement.setLong(FIRST_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "deleteEntities method was completed successfully. Meals with id: " + idList + " were deleted");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete meals from database. Database access error:", e);
            throw new DaoException("Impossible to delete meals from database. Database access error:", e);
        }
    }

    @Override
    public boolean isMealExist(String title) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEAL_BY_TITLE)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isMealExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of meal. Database access error:", e);
            throw new DaoException("Impossible to check existence of meal. Database access error:", e);
        }
    }

    @Override
    public void updateMealStatuses(boolean status, List<Long> idList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MEAL_STATUS)) {
            for (Long mealId : idList) {
                statement.setBoolean(FIRST_PARAM_INDEX, status);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "updateMealStatuses method was completed successfully.");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update meal statuses. Database access error:", e);
            throw new DaoException("Impossible to update meal statuses. Database access error:", e);
        }
    }
}