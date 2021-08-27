package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.MenuDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.model.dao.ColumnName.*;

/**
 *  The {@link MenuDaoImpl} class provides access to
 *  menus table in the database
 */
public class MenuDaoImpl implements MenuDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String FIND_MENU_BY_ID = "SELECT title, menu_types.type, created, updated FROM menus" +
            " JOIN menu_types ON menu_types.id=type_id" +
            " WHERE menus.id=?";
    private static final String FIND_MENU_BY_TITLE = "SELECT id FROM menus WHERE title=?";
    private static final String FIND_ALL_MENUS = "SELECT menus.id, title, menu_types.type, created, updated FROM menus" +
            " JOIN menu_types ON menu_types.id=type_id";
    private static final String FIND_MEALS_FOR_MENU = "SELECT meals.id, title, image, meal_types.type, " +
            "price, recipe, created, active FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id" +
            " JOIN available_meals ON meal_id=meals.id AND menu_id=?";
    private static final String FIND_AVAILABLE_MEALS_FOR_MENU = "SELECT meals.id, title, image, meal_types.type, " +
            "price, recipe, created, active FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id" +
            " JOIN available_meals ON meal_id=meals.id AND menu_id=?" +
            " WHERE active=1";
    private static final String FIND_MEAL_BY_ID_AND_TYPE = "SELECT id FROM meals WHERE id=? AND type_id=?";
    private static final String MEAL_ORDER_BY_TITLE = " ORDER BY meals.title ";
    private static final String INSERT_NEW_MENU = "INSERT INTO menus (title, type_id, created, updated) VALUES(?, ?, ?, ?)";
    private static final String INSERT_MEAL_TO_MENU = "INSERT INTO available_meals (menu_id, meal_id) VALUES(?, ?)";
    private static final String UPDATE_MENU_TITLE = "UPDATE menus SET title=? WHERE id=?";
    private static final String DELETE_MENU_BY_ID = "DELETE FROM menus WHERE id=?";
    private static final String DELETE_MEAL_FROM_MENU = "DELETE FROM available_meals WHERE menu_id=? AND meal_id=?";

    @Override
    public Optional<Menu> findEntityById(long id) throws DaoException {
        Optional<Menu> menuOptional = Optional.empty();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(id);
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(Menu.Type.valueOf(resultSet.getString(MENU_TYPES_TYPE).toUpperCase()));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
                menuOptional = Optional.of(menu);
            }
            logger.log(Level.DEBUG, "findEntityById method was completed successfully." +
                    (menuOptional.isPresent() ? " Menu with id " + id + " was found" : " Menu with id " + id + " doesn't exist"));
            return menuOptional;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find menu by id. Database access error:", e);
            throw new DaoException("Impossible to find menu by id. Database access error:", e);
        }
    }

    @Override
    public List<Menu> findAllEntities() throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_MENUS)) {
            List<Menu> menus = new ArrayList<>();
            while (resultSet.next()) {
                Menu menu = new Menu();
                menu.setId(resultSet.getLong(MENU_ID));
                menu.setTitle(resultSet.getString(MENU_TITLE));
                menu.setType(Menu.Type.valueOf(resultSet.getString(MENU_TYPES_TYPE).toUpperCase()));
                menu.setCreated(resultSet.getDate(MENU_CREATED).toLocalDate());
                menu.setUpdated(resultSet.getDate(MENU_UPDATED).toLocalDate());
                menus.add(menu);
            }
            logger.log(Level.DEBUG, "findAllEntities method was completed successfully. " + menus.size() + " menus were found");
            return menus;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find all menus. Database access error:", e);
            throw new DaoException("Impossible to find all menus. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(Menu menu) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_MENU, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_PARAM_INDEX, menu.getTitle());
            statement.setInt(SECOND_PARAM_INDEX, menu.getType().ordinal() + 1);
            statement.setDate(THIRD_PARAM_INDEX, Date.valueOf(menu.getCreated()));
            statement.setDate(FOURTH_PARAM_INDEX, Date.valueOf(menu.getUpdated()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long menuId = 0;
            if (resultSet.next()) {
                menuId = resultSet.getLong(FIRST_PARAM_INDEX);
                logger.log(Level.INFO, "insertNewEntity method was completed successfully. Menu with id " + menuId + " was added");
            }
            return menuId;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert menu into database. Database access error:", e);
            throw new DaoException("Impossible to insert menu into database. Database access error:", e);
        }
    }

    @Override
    public long insertNewEntity(Menu menu, List<Long> mealIdList) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement menuStatement = connection.prepareStatement(INSERT_NEW_MENU, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement mealStatement = connection.prepareStatement(INSERT_MEAL_TO_MENU);
                 PreparedStatement checkStatement = connection.prepareStatement(FIND_MEAL_BY_ID_AND_TYPE)) {
                menuStatement.setString(FIRST_PARAM_INDEX, menu.getTitle());
                menuStatement.setInt(SECOND_PARAM_INDEX, menu.getType().ordinal() + 1);
                menuStatement.setDate(THIRD_PARAM_INDEX, Date.valueOf(menu.getCreated()));
                menuStatement.setDate(FOURTH_PARAM_INDEX, Date.valueOf(menu.getUpdated()));
                menuStatement.executeUpdate();
                ResultSet resultSet = menuStatement.getGeneratedKeys();
                long menuId = 0;
                if (resultSet.next()) {
                    menuId = resultSet.getLong(FIRST_PARAM_INDEX);
                    for (Long mealId : mealIdList) {
                        checkStatement.setLong(FIRST_PARAM_INDEX, mealId);
                        checkStatement.setInt(SECOND_PARAM_INDEX, menu.getType().ordinal() + 1);
                        ResultSet check = checkStatement.executeQuery();
                        if (check.next()) {
                            mealStatement.setLong(FIRST_PARAM_INDEX, menuId);
                            mealStatement.setLong(SECOND_PARAM_INDEX, mealId);
                            mealStatement.addBatch();
                        }
                    }
                    mealStatement.executeBatch();
                }
                connection.commit();
                logger.log(Level.INFO, "insertNewEntity method was completed successfully. Menu with id " + menuId + " was added");
                return menuId;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                logger.log(Level.ERROR, "Change cancellation error in the current transaction:", throwables);
            }
            logger.log(Level.ERROR, "Impossible to insert menu into database. Database access error:", e);
            throw new DaoException("Impossible to insert menu into database. Database access error:", e);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException throwables) {
                logger.log(Level.ERROR, "Database access error occurs:", throwables);
            }
        }
    }

    @Override
    public boolean deleteEntityById(long id) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MENU_BY_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, id);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, "deleteEntityById method was completed successfully."
                    + (result ? " Menu with id " + id + " was deleted" : " Menu with id " + id + " wasn't deleted"));
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete menu from database. Database access error:", e);
            throw new DaoException("Impossible to delete menu from database. Database access error:", e);
        }
    }

    @Override
    public boolean deleteEntitiesById(List<Long> idList) {
        throw new UnsupportedOperationException("deleteEntitiesById(List<Long> idList) method is not supported");
    }

    @Override
    public boolean isMenuExist(String title) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
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
    public boolean insertMealToMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEAL_TO_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, result ? "insertMealToMenu method was completed successfully. Meal with id "
                    + mealId + " was added to menu with id " + menuId : "Meal with id " + mealId + " wasn't added to menu with id " + menuId);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to add meal to menu. Database access error:", e);
            throw new DaoException("Impossible to add meal to menu. Database access error:", e);
        }
    }

    @Override
    public boolean updateMenuTitle(long menuId, String title) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MENU_TITLE)) {
            statement.setString(FIRST_PARAM_INDEX, title);
            statement.setLong(SECOND_PARAM_INDEX, menuId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, result ? "updateMenuTitle method was completed successfully. Menu title with menu id "
                    + menuId + " was updated to " + title : "Menu title menu with id " + menuId + " wasn't updated to " + title);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to update menu title. Database access error:", e);
            throw new DaoException("Impossible to update menu title. Database access error:", e);
        }
    }

    @Override
    public List<Meal> findMealsForMenu(long menuId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEALS_FOR_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            ResultSet resultSet = statement.executeQuery();
            List<Meal> meals = new ArrayList<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "findMealsForMenu method was completed successfully." +
                    (!meals.isEmpty() ? meals.size() + " meals were found for menu with id " + menuId : " Menu doesn't have any meals"));
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for menu. Database access error:", e);
            throw new DaoException("Impossible to find meals for menu. Database access error:", e);
        }
    }

    @Override
    public int getAvailableMealCountForMenu(long menuId) throws DaoException {
        int result = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM (" + FIND_AVAILABLE_MEALS_FOR_MENU + ") as tbl")
        ) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(FIRST_PARAM_INDEX);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Can't count row number.", e);
            throw new DaoException("Can't count row number.", e);
        }
        return result;
    }

    @Override
    public List<Meal> findAvailableMealsForMenu(long menuId, int page) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(buildPaginatedQuery(FIND_AVAILABLE_MEALS_FOR_MENU + MEAL_ORDER_BY_TITLE, page))) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            ResultSet resultSet = statement.executeQuery();
            List<Meal> meals = new ArrayList<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                meals.add(meal);
            }
            logger.log(Level.DEBUG, "findMealsForMenuByPresence method was completed successfully." +
                    (!meals.isEmpty() ? meals.size() + " meals were found for menu with id " + menuId : " Menu doesn't have any meals"));
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for menu. Database access error:", e);
            throw new DaoException("Impossible to find meals for menu. Database access error:", e);
        }
    }

    @Override
    public boolean deleteMealFromMenu(long menuId, long mealId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEAL_FROM_MENU)) {
            statement.setLong(FIRST_PARAM_INDEX, menuId);
            statement.setLong(SECOND_PARAM_INDEX, mealId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, result ? "deleteMealFromMenu method was completed successfully. Meal with id " + mealId
                    + " was deleted from menu with id " + menuId : "Meal with id " + mealId + " wasn't deleted from menu with id " + menuId);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to delete meal from menu. Database access error:", e);
            throw new DaoException("Impossible to delete meal from menu. Database access error:", e);
        }
    }
}