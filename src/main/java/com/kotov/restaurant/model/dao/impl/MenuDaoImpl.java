package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.MenuDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
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

public class MenuDaoImpl implements MenuDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_MENU_BY_TITLE = "SELECT id FROM menus WHERE title=?";
    private static final String FIND_MENU_BY_ID = "SELECT title, type, created, updated FROM menus WHERE id=?";
    private static final String FIND_ALL_MENUS = "SELECT id, title, type, created, updated FROM menus";
    private static final String FIND_ALL_MEALS_FOR_MENU = "SELECT meals.id, title, image, type, price, recipe, created, active" +
            " FROM meals JOIN available_meals ON meal_id=meals.id AND menu_id=?";
    private static final String DELETE_MENU_BY_ID = "DELETE FROM menus WHERE id=?";
    private static final String DELETE_MEAL_FROM_MENU = "DELETE FROM available_meals WHERE menu_id=? AND meal_id=?";
    private static final String INSERT_MEAL_TO_MENU = "INSERT INTO available_meals (menu_id, meal_id) VALUES(?, ?)";
    private static final String INSERT_NEW_MENU = "INSERT INTO menus (title, type, created, updated) VALUES(?, ?, ?, ?)";

    @Override
    public Optional<Menu> findEntityById(long id) throws DaoException {
        Optional<Menu> menuOptional = Optional.empty();
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(id);
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(resultSet.getString(MENU_TYPE));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
                menuOptional = Optional.of(menu);
                logger.log(Level.DEBUG, "Method findEntityById was completed successfully. Menu: " + menu);
            }
            return menuOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find menu by id. Database access error:", e);
            throw new DaoException("Impossible to find menu by id. Database access error:", e);
        }
    }

    @Override
    public List<Menu> findAllEntities() throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_MENUS)) {
            List<Menu> menus = new ArrayList<>();
            while (resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(resultSet.getLong(MENU_ID));
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(resultSet.getString(MENU_TYPE));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
                menus.add(menu);
            }
            logger.log(Level.DEBUG, "Method findAllEntity was completed successfully. Meals: " + menus);
            return menus;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find all menus. Database access error:", e);
            throw new DaoException("Impossible to find all menus. Database access error:", e);
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
            logger.log(Level.DEBUG, "Method insertNewEntity was completed successfully. Menu with id " + key + " was added");
            return key;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert menu into database. Database access error:", e);
            throw new DaoException("Impossible to insert menu into database. Database access error:", e);
        }
    }

    @Override
    public boolean deleteEntityById(long id) throws DaoException {
        boolean result = false;
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            if (statement.executeUpdate() == 1) {
                result = true;
            }
            logger.log(Level.DEBUG, "Method deleteEntityById was completed successfully. Menu with id " + id + " was deleted");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete menu from database. Database access error:", e);
            throw new DaoException("Impossible to delete menu from database. Database access error:", e);
        }
        return result;
    }

    @Override
    public void deleteEntitiesById(List<Long> idList) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMenuExist(String title) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_TITLE)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            ResultSet resultSet = statement.executeQuery();
            boolean result = resultSet.isBeforeFirst();
            logger.log(Level.DEBUG, "Method isMenuExist was completed successfully. Result: " + result);
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
            logger.log(Level.DEBUG, "Method addMealToMenu was completed successfully: Meal with id "
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
            logger.log(Level.DEBUG, "Method addMealsToMenu was completed successfully. Meals with id: "
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
                meal.setId(resultSet.getLong(MEAL_ID));
                meal.setTitle(resultSet.getString(MEAL_TITLE));
                meal.setImage(encodeBlob(resultSet.getBlob(MEAL_IMAGE)));
                meal.setType(resultSet.getString(MEAL_TYPE));
                meal.setPrice(resultSet.getBigDecimal(MEAL_PRICE));
                meal.setRecipe(resultSet.getString(MEAL_RECIPE));
                meal.setCreated(LocalDateTime.parse(resultSet.getString(MEAL_CREATED), FORMATTER));
                meal.setActive(resultSet.getBoolean(MEAL_ACTIVE));
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "Method findAllMealsForMenu was completed successfully. Meals: " + meals +
                    " were found for menu with id " + menuId);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for menu. Database access error:", e);
            throw new DaoException("Impossible to find meals for menu. Database access error:", e);
        }
    }

    private String encodeBlob(Blob image) throws DaoException {                             // to util package
        try {
            byte[] imageBytes = image.getBinaryStream().readAllBytes();
            byte[] encodeBase64 = Base64.getEncoder().encode(imageBytes);
            String base64DataString = new String(encodeBase64, StandardCharsets.UTF_8);
            String src = "data:image/jpeg;base64," + base64DataString;                      // to constant
            return src;
        } catch (SQLException e) {
            throw new DaoException("Image InputStream cannot be received. Error accessing BLOB value:", e);
        } catch (IOException e) {
            throw new DaoException("Image bytes cannot be read from InputStream:", e);
        }
    }

    @Override
    public void deleteMealFromMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL_FROM_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            statement.executeUpdate();
            logger.log(Level.DEBUG, "Method deleteMealFromMenu was completed successfully. Meal with id " + mealId
                    + "was deleted from menuId with id " + menuId);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete meal from menu. Database access error:", e);
            throw new DaoException("Impossible to delete meal from menu. Database access error:", e);
        }
    }
}