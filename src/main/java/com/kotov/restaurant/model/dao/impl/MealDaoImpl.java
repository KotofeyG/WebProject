package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MealDaoImpl implements MealDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();

    private static final String FIND_MEAL_BY_TITLE = "SELECT id FROM meals WHERE title=?";
    private static final String FIND_MEAL_BY_ID = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id WHERE meals.id=?";
    private static final String FIND_ALL_MEALS = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id";
    private static final String FIND_ALL_MEALS_BY_TYPE = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id AND meal_types.type=?";
    private static final String INSERT_NEW_MEAL = "INSERT INTO meals (title, image, type_id, price, recipe, created, active) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MEAL_TO_CART = "INSERT INTO carts (user_id, meal_id, quantity) VALUES(?, ?, ?)";
    private static final String UPDATE_MEAL_STATUS = "UPDATE meals SET active=? WHERE id=?";
    private static final String DELETE_MEAL = "DELETE FROM meals WHERE id=?";

    @Override
    public Optional<Meal> findEntityById(long id) throws DaoException {
        Optional<Meal> mealOptional = Optional.empty();
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEAL_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                mealOptional = Optional.of(meal);
            }
            logger.log(Level.DEBUG, "findEntityById method was completed successfully."
                    + ((mealOptional.isPresent()) ? " Meal with id " + id + " was found" : " Meal with id " + id + " don't exist"));
            return mealOptional;
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
                Meal meal = MealCreator.create(resultSet);
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "findAllEntities method was completed successfully. All meals were found");
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find all meals. Database access error:", e);
            throw new DaoException("Impossible to find all meals. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(Meal meal) {
        throw new UnsupportedOperationException("insertNewEntity(Meal meal) method is not supported");
    }

    @Override
    public boolean deleteEntityById(long id) {
        return false;
    }

    @Override
    public long insertNewEntity(Meal meal, InputStream image) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_MEAL, RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, meal.getTitle());
            statement.setBlob(SECOND_PARAM_INDEX, image);
            statement.setInt(THIRD_PARAM_INDEX, meal.getType().ordinal() + 1);
            statement.setBigDecimal(FOURTH_PARAM_INDEX, meal.getPrice());
            statement.setString(FIFTH_PARAM_INDEX, meal.getRecipe());
            statement.setTimestamp(SIXTH_PARAM_INDEX, Timestamp.valueOf(meal.getCreated()));
            statement.setBoolean(SEVENTH_PARAM_INDEX, meal.isActive());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long mealId = 0;
            if (resultSet.next()) {
                mealId = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.INFO, "insertNewEntity method was completed successfully. Meal with id " + mealId + " was added");
            return mealId;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert meal into database. Database access error:", e);
            throw new DaoException("Impossible to insert meal into database. Database access error:", e);
        }
    }

    @Override
    public void deleteEntitiesById(List<Long> idList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL)) {
            for (Long mealId : idList) {
                statement.setLong(FIRST_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "deleteEntities method was completed successfully. Meals with id " + idList + " were deleted");
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
    public List<Meal> findMealsByType(Meal.Type type) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_MEALS_BY_TYPE)) {
            statement.setString(FIRST_PARAM_INDEX, type.toString());
            ResultSet resultSet = statement.executeQuery();
            List<Meal> meals = new ArrayList<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "findMealsByType method was completed successfully. " + meals.size()
                    + " meals with type " + type + " were found");
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals by type. Database access error:", e);
            throw new DaoException("Impossible to find meals by type. Database access error:", e);
        }
    }

    @Override
    public void updateMealStatusesById(boolean status, List<Long> mealIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MEAL_STATUS)) {
            for (Long mealId : mealIdList) {
                statement.setBoolean(FIRST_PARAM_INDEX, status);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.INFO, "updateMealStatusesById method was completed successfully. Meal statuses with meal id list "
                    + mealIdList + " were updated to " + status + " statuses");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update meal statuses. Database access error:", e);
            throw new DaoException("Impossible to update meal statuses. Database access error:", e);
        }
    }

    @Override
    public boolean insertMealToUserCart(long userId, long mealId, int mealQuantity) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEAL_TO_CART)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.setInt(THIRD_PARAM_INDEX,mealQuantity);
            int rowCount = statement.executeUpdate();
            logger.log(Level.DEBUG, "insertMealToUserCart method was completed successfully. Meal with id "
                    + mealId + " was added to user cart with user id " + userId + " in the amount of " + mealQuantity + " pieces");
            return rowCount == 1;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert meal(s) to user cart. Database access error:", e);
            throw new DaoException("Impossible to insert meal(s) to user cart. Database access error:", e);
        }
    }
}