package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
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

public class MenuDaoImpl implements MenuDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_MENU_BY_TITLE = "SELECT id FROM menus WHERE TITLE=?";
    private static final String FIND_MENU_BY_ID = "SELECT title, type, created, updated FROM menus WHERE id=?";
    private static final String FIND_ALL_MENUS = "SELECT id, title, type, created, updated FROM menus";
    private static final String FIND_ALL_MEALS_FOR_MENU = "SELECT meals.id, title, image, type, price, recipe, created, active" +
            " FROM meals JOIN available_meals ON meal_id=meals.id AND menu_id=?";
    private static final String DELETE_MEAL_FROM_MENU = "DELETE FROM available_meals WHERE menu_id=? AND meal_id=?";
    private static final String INSERT_MEAL_TO_MENU = "INSERT INTO available_meals (menu_id, meal_id) VALUES(?, ?)";
    private static final String INSERT_NEW_MENU = "INSERT INTO menus (title, type, created, updated) VALUES(?, ?, ?, ?)";

    @Override
    public Menu findEntityById(long id) throws DaoException {                                     // to do <List> meals?
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            Menu menu = new Menu();
            if (resultSet.next()) {
                menu.setMenuId(id);
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(resultSet.getString(MENU_TYPE));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
            }
            logger.log(Level.DEBUG, "findEntityById method was completed successfully. Menu: " + menu);
            return menu;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find menu by id. Database access error:", e);
            throw new DaoException("Impossible to find menu by id. Database access error:", e);
        }
    }

    @Override
    public List<Menu> findAllEntities() throws DaoException {                                       // to do <List> meals?
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
            logger.log(Level.DEBUG, "findAllEntity method was completed successfully. Meals: " + menus);
            return menus;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find menus. Database access error:", e);
            throw new DaoException("Impossible to find menus. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(Menu menu) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_MENU, Statement.RETURN_GENERATED_KEYS)) {
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
            logger.log(Level.DEBUG, "insertNewEntity method was completed successfully. Menu " + menu + " was added");
            return key;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert menu into database. Database access error:", e);
            throw new DaoException("Impossible to insert menu into database. Database access error:", e);
        }
    }

    @Override
    public void deleteEntities(List<Long> idList) throws DaoException {                                              //to do
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMenuExist(String title) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_TITLE)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "isMenuExist method was completed successfully. Result: " + result);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check existence of menu. Database access error:", e);
            throw new DaoException("Impossible to check existence of menu. Database access error:", e);
        }
    }

    @Override
    public void insertMealToMenu(long menuId, long mealId) throws DaoException {                  // to delete this method?
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEAL_TO_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "addMealToMenu method was completed successfully: Meal with id "
                    + mealId + "was added to menu with id " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to add meal to menu. Database access error:", e);
            throw new DaoException("Impossible to add meal to menu. Database access error:", e);
        }
    }

    @Override
    public void insertMealsToMenu(long menuId, List<Long> mealIdList) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEAL_TO_MENU)) {
            for (Long mealId : mealIdList) {
                statement.setLong(FIRST_PARAM_INDEX, menuId);
                statement.setLong(SECOND_PARAM_INDEX, mealId);
                statement.addBatch();
            }
            statement.executeBatch();
            logger.log(Level.DEBUG, "addMealsToMenu method was completed successfully. Meals with id: "
                    + mealIdList + " were added to menu with id " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to add meals to menu. Database access error:", e);
            throw new DaoException("Impossible to add meals to menu. Database access error:", e);
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
            logger.log(Level.DEBUG, "findAllMealsForMenu method was completed successfully. Meals: " + meals);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for menu. Database access error:", e);
            throw new DaoException("Impossible to find meals for menu. Database access error:", e);
        }
    }

    @Override
    public void deleteMealFromMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL_FROM_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "deleteMealFromMenu method was completed successfully. Meal with id " + mealId
                    + "was deleted from menuId with id " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete meal from menu. Database access error:", e);
            throw new DaoException("Impossible to delete meal from menu. Database access error:", e);
        }
    }
}