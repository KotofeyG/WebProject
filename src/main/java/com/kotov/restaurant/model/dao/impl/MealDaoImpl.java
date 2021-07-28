package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
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

public class MealDaoImpl implements MealDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String CHECK_MEAL = "SELECT title FROM meals WHERE TITLE=?";
    private static final String CHECK_MENU = "SELECT title FROM menus WHERE TITLE=?";
    private static final String FIND_MEAL_BY_ID = "SELECT title, image, type, price, recipe, created FROM meals WHERE id=?";
    private static final String FIND_MENU_BY_ID = "SELECT title, type, created, updated FROM menus WHERE id=?";
    private static final String FIND_ALL_MEALS = "SELECT meals.id, title, image, type, price, recipe, created, active FROM meals";
    private static final String FIND_ALL_MENUS = "SELECT id, title, type, created, updated FROM menus";
    private static final String FIND_ALL_MEALS_FOR_MENU = "SELECT meals.id, title, image, type, price, recipe, created, active" +
            " FROM meals JOIN available_meals ON meal_id=meals.id AND menu_id=?";
    private static final String DELETE_MEAL_FROM_MENU = "DELETE FROM available_meals WHERE menu_id=? AND meal_id=?";
    private static final String ADD_MEAL_TO_MENU = "INSERT INTO available_meals (menu_id, meal_id) VALUES(?, ?)";
    private static final String ADD_NEW_MEAL = "INSERT INTO meals" +
            " (title, image, type, price, recipe, created, active) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String ADD_NEW_MENU = "INSERT INTO menus" +
            " (title, type, created, updated) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_MEAL_STATUS = "UPDATE meals SET active=? WHERE id=?";
    private static final String DELETE_MEAL = "DELETE FROM meals WHERE meals.id=?";

    @Override
    public boolean isMealExist(String title) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_MEAL)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            ResultSet set = statement.executeQuery();
            boolean result = set.isBeforeFirst();
            logger.log(Level.DEBUG, "isMealExist() method was completed successfully. Result is: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isMenuExist(String title) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_MENU)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            ResultSet set = statement.executeQuery();
            boolean result = set.isBeforeFirst();
            logger.log(Level.DEBUG, "isMenuExist() method was completed successfully. Result is: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void addNewMeal(Meal meal) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_MEAL)) {
            statement.setString(FIRST_PARAM_INDEX, meal.getTitle());
            statement.setBlob(SECOND_PARAM_INDEX, meal.getImage());
            statement.setString(THIRD_PARAM_INDEX, meal.getType());
            statement.setBigDecimal(FOURTH_PARAM_INDEX, meal.getPrice());
            statement.setString(FIFTH_PARAM_INDEX, meal.getRecipe());
            statement.setTimestamp(SIXTH_PARAM_INDEX, Timestamp.valueOf(meal.getCreated()));
            statement.setBoolean(SEVENTH_PARAM_INDEX, meal.isActive());
            statement.executeUpdate();
            logger.log(Level.DEBUG, "addNewMeal() method was completed successfully. Result is " + meal);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public long addNewMenu(Menu menu) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_MENU, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, menu.getTitle());
            statement.setString(SECOND_PARAM_INDEX, menu.getType());
            statement.setDate(THIRD_PARAM_INDEX, Date.valueOf(menu.getCreated()));
            statement.setDate(FOURTH_PARAM_INDEX, Date.valueOf(menu.getUpdated()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long key = 0;
            if (resultSet.next()) {
                key = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.DEBUG, "addNewMenu() method was completed successfully. Result is " + menu);
            return key;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void addMealsToMenu(long menuId, List<Long> mealIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_MEAL_TO_MENU)) {
            for (Long mealId : mealIdList) {
                statement.setLong(FIRST_PARAM_INDEX, menuId);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "addMealToMenu() method was completed successfully.");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void updateMealStatuses(boolean status, List<Long> mealIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MEAL_STATUS)) {
            for (Long mealId : mealIdList) {
                statement.setBoolean(FIRST_PARAM_INDEX, status);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "updateStatuses() method was completed successfully.");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void removeMeals(List<Long> mealIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL)) {
            for (Long mealId : mealIdList) {
                statement.setLong(FIRST_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "removeMeals() method was completed successfully. Meals id are: " + mealIdList);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void addMealToMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_MEAL_TO_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "Meal was added successfully: mealId " + mealId + " from menuId " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteMealFromMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL_FROM_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "Meal was deleted successfully: mealId " + mealId + " from menuId " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public Meal getMealById(long id) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEAL_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            Meal meal = new Meal();
            if (resultSet.next()) {
                meal.setMealId(resultSet.getLong(MEAL_ID));
                meal.setTitle(resultSet.getString(MEAL_TITLE));
                meal.setImage(resultSet.getBlob(MEAL_IMAGE));
                meal.setType(resultSet.getString(MEAL_TYPE));
                meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
                meal.setRecipe(resultSet.getString(MEAL_RECIPE));
                meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
                meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
                logger.log(Level.DEBUG, "Meal was found: " + meal);
            }
            return meal;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public Menu findMenuById(long menuId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            ResultSet resultSet = statement.executeQuery();
            Menu menu = new Menu();
            if (resultSet.next()) {
                menu.setMenuId(menuId);
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(resultSet.getString(MENU_TYPE));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
            }
            logger.log(Level.DEBUG, "Menu was found: " + menu);
            return menu;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Meal> findAllMeals() throws DaoException {
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
            logger.log(Level.DEBUG, "Meals were found: " + meals);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Meal> findAllMealsForMenu(long menuId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_MEALS_FOR_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            ResultSet resultSet = statement.executeQuery();
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
            logger.log(Level.DEBUG, "Meals were found: " + meals);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Menu> findAllMenus() throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_MENUS)) {
            List<Menu> menus = new ArrayList<>();
            while (resultSet.next()) {
                Menu menu = new Menu();
                menu.setMenuId(resultSet.getLong(MENU_ID));
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(resultSet.getString(MENU_TYPE));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
                menus.add(menu);
            }
            logger.log(Level.DEBUG, "Meals were found: " + menus);
            return menus;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database access error:", e);
            throw new DaoException(e);
        }
    }
}