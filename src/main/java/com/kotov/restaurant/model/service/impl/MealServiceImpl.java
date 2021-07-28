package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao1;
import com.kotov.restaurant.model.dao.MenuDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.pool.ConnectionPool;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.validator.MealValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealServiceImpl implements MealService {
    private static final Logger logger = LogManager.getLogger();
    private static final MealDao1 mealDao = DaoProvider.getInstance().getMealDao();
    private static final MenuDao menuDao = DaoProvider.getInstance().getMenuDao();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();

    @Override
    public boolean addNewMeal(Map<String, String> dataCheckResult) throws ServiceException {       // change realization
        boolean result = false;

        String title = dataCheckResult.get(TITLE);
        String type = dataCheckResult.get(TYPE);
        String priceStr = dataCheckResult.get(PRICE);
        String recipe = dataCheckResult.get(RECIPE);
        String imageStr = dataCheckResult.get(IMAGE);
        try {
            if (MealValidator.areMealParamsValid(dataCheckResult)) {
                if (!mealDao.isMealExist(title)) {
                    File imageFile = new File(PATH_TO_IMAGES + imageStr);
                    logger.log(Level.DEBUG, "Does file exist: " + imageFile.exists());
                    String fileFormat = imageStr.substring(imageStr.lastIndexOf(DOT) + 1);
                    Connection connection = null;
                    OutputStream outputStream = null;
                    Meal meal;
                    try {
                        connection = connection_pool.getConnection();
                        Blob imageBlob = connection.createBlob();
                        outputStream = imageBlob.setBinaryStream(FIRST_POSITION);
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        ImageIO.write(bufferedImage, fileFormat, outputStream);
                        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceStr));
                        meal = new Meal(title, imageBlob, type, price, recipe, LocalDateTime.now(), ACTIVE);
                    } finally {
                        outputStream.close();
                        connection.close();
                    }
                    mealDao.insertNewEntity(meal);
                    result = true;
                } else {
                    dataCheckResult.clear();
                    dataCheckResult.put(INVALID_MEAL, NOT_UNIQUE_MEAL_TITLE);
                }
            } else {
                dataCheckResult.clear();
                dataCheckResult.put(INVALID_MEAL, INVALID_MEAL_REGISTERED_FIELDS);
            }
        } catch (DaoException | SQLException | IOException e) {
            logger.log(Level.ERROR, "Meal cannot be added:", e);
            throw new ServiceException("Meal cannot be added:", e);
        }
        logger.log(Level.DEBUG, getClass().getSimpleName() + "is completed successfully. Result is: " + result);
        return result;
    }

    @Override
    public boolean addNewMenu(String title, String type, String[] mealsId) throws ServiceException {
        boolean result = false;
        try {
            if (!menuDao.isMenuExist(title)) {
                Menu menu = new Menu(title, type);
                long menuId = menuDao.insertNewEntity(menu);
                List<Long> mealIdList = convertArrayToList(mealsId);
                menuDao.insertMealsToMenu(menuId, mealIdList);
                result = true;
            }
            logger.log(Level.DEBUG, "addNewMenu service method is completed successfully. Result is: " + result);
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menu cannot be added:", e);
            throw new ServiceException("Menu cannot be added:", e);
        }
    }

    @Override
    public void updateMealStatuses(boolean status, String[] mealIdArray) throws ServiceException {
        List<Long> mealIdList = convertArrayToList(mealIdArray);
        try {
            mealDao.updateMealStatuses(status, mealIdList);
            logger.log(Level.DEBUG, "updateStatuses service method is completed successfully.");
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal statuses cannot be updated:", e);
            throw new ServiceException("Meal statuses cannot be updated:", e);
        }
    }

    @Override
    public void removeMeals(String[] mealIdArray) throws ServiceException {
        List<Long> mealIdList = convertArrayToList(mealIdArray);
        try {
            mealDao.deleteEntities(mealIdList);
            logger.log(Level.DEBUG, "removeMeals service method is completed successfully.");
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meals cannot be removed:", e);
            throw new ServiceException("Meals cannot be removed:", e);
        }
    }

    @Override
    public void addMealToMenu(String menuIdStr, String mealIdStr) throws ServiceException {
        try {
            long menuId = Long.parseLong(menuIdStr);
            long mealId = Long.parseLong(mealIdStr);
            menuDao.insertMealToMenu(menuId, mealId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be added from menu:", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteMealFromMenu(String menuIdStr, String mealIdStr) throws ServiceException {
        try {
            long menuId = Long.parseLong(menuIdStr);
            long mealId = Long.parseLong(mealIdStr);
            menuDao.deleteMealFromMenu(menuId, mealId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be deleted from menu:", e);
            throw new ServiceException(e);
        }
    }

    private List<Long> convertArrayToList(String[] mealIdArray) {
        List<Long> mealIdList = new ArrayList<>();
        for (String mealId : mealIdArray) {
            mealIdList.add(Long.parseLong(mealId));
        }
        return mealIdList;
    }

    @Override
    public List<Meal> findAllMeals() throws ServiceException {
        try {
            return mealDao.findAllEntities();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meals cannot be found:", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Menu> findAllMenu() throws ServiceException {
        try {
            List<Menu> menus = menuDao.findAllEntities();
            for (Menu menu : menus) {
                menu.addAll(menuDao.findAllMealsForMenu(menu.getMenuId()));
            }
            return menus;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menus cannot be found:", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public Menu findMenuById(String menuIdStr) throws ServiceException {
        long id = Long.parseLong(menuIdStr);
        try {
            Menu menu = menuDao.findEntityById(id);
            menu.addAll(menuDao.findAllMealsForMenu(id));
            return menu;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menu cannot be found:", e);
            throw new ServiceException(e);
        }
    }
}