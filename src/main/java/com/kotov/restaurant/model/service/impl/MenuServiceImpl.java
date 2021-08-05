package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.dao.MenuDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.validator.MealValidator;
import com.kotov.restaurant.model.service.validator.MenuValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class MenuServiceImpl implements MenuService {
    private static final Logger logger = LogManager.getLogger();
    private static final MealDao mealDao = DaoProvider.getInstance().getMealDao();
    private static final MenuDao menuDao = DaoProvider.getInstance().getMenuDao();

    @Override
    public boolean addNewMeal(Map<String, String> dataCheckResult, InputStream image) throws ServiceException {
        boolean result = false;
        String title = dataCheckResult.get(TITLE);
        String type = dataCheckResult.get(TYPE);
        String priceStr = dataCheckResult.get(PRICE);
        String recipe = dataCheckResult.get(RECIPE);
        try {
            if (MealValidator.areMealParamsValid(dataCheckResult, image)) {
                if (!mealDao.isMealExist(title)) {
                    BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceStr));
                    Meal meal = new Meal(title, Meal.Type.valueOf(type.toUpperCase()), price, recipe, LocalDateTime.now());
                    mealDao.insertNewEntity(meal, image);
                    result = true;
                } else {
                    dataCheckResult.clear();
                    dataCheckResult.put(MEAL_CREATION_DATA, NOT_UNIQUE);
                }
            } else {
                dataCheckResult.clear();
                dataCheckResult.put(MEAL_CREATION_DATA, INVALID);
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be added:", e);
            throw new ServiceException("Meal cannot be added:", e);
        }
        logger.log(Level.DEBUG, "Method addNewMeal is completed successfully. Result is: " + result);
        return result;
    }

    @Override
    public boolean addNewMenu(String title, String type, String[] mealIdArray) throws ServiceException {
        boolean result = false;
        try {
            if (MenuValidator.areMenuParametersValid(title, type, mealIdArray) && !menuDao.isMenuExist(title)) {
                Menu menu = new Menu(title, Menu.Type.valueOf(type.toUpperCase()));
                List<Long> mealIdList = convertArrayToList(mealIdArray);
                long menuId = menuDao.insertNewEntity(menu);                                // autocommit(false)?
                menuDao.insertMealsToMenu(menuId, mealIdList);
                result = true;
            }
            logger.log(Level.DEBUG, "Method addNewMenu is completed successfully. Result is: " + result);
            return result;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Method addNewMenu cannot be completed:", e);
            throw new ServiceException("Method addNewMenu cannot be completed:", e);
        }
    }

    @Override
    public boolean updateMealStatusesById(boolean status, String[] mealIdArray) throws ServiceException {
        boolean result = false;
        if (mealIdArray != null) {
            List<Long> mealIdList = convertArrayToList(mealIdArray);
            try {
                mealDao.updateMealStatusesById(status, mealIdList);
                result = true;
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Meal statuses cannot be updated:", e);
                throw new ServiceException("Meal statuses cannot be updated:", e);
            }
        }
        logger.log(Level.DEBUG, "Method updateStatuses is completed successfully. Result is: " + result);
        return result;
    }

    @Override
    public List<Meal> findMealsForMenu(long menuId) throws ServiceException {
        try {
            return menuDao.findMealsForMenu(menuId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be found:", e);
            throw new ServiceException("Meal cannot be found:", e);
        }
    }

    @Override
    public boolean removeMealsById(String[] mealIdArray) throws ServiceException {
        boolean result = false;
        if (mealIdArray != null) {
            try {
                List<Long> mealIdList = convertArrayToList(mealIdArray);
                mealDao.deleteEntitiesById(mealIdList);
                result = true;
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Meals cannot be removed:", e);
                throw new ServiceException("Meals cannot be removed:", e);
            }
        }
        logger.log(Level.DEBUG, "Method removeMeals is completed successfully. Result is: " + result);
        return result;
    }

    private List<Long> convertArrayToList(String[] idArray) throws ServiceException {
        List<Long> idList = new ArrayList<>();
        try {
            for (String idStr : idArray) {
                idList.add(Long.parseLong(idStr));
            }
            return idList;
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Variable idStr does not contain a parsable long:", e);
            throw new ServiceException("Variable idStr does not contain a parsable long:", e);
        }
    }

    @Override
    public void addMealToMenu(String menuIdStr, String mealIdStr) throws ServiceException {
        try {
            long menuId = Long.parseLong(menuIdStr);
            long mealId = Long.parseLong(mealIdStr);
            menuDao.insertMealToMenu(menuId, mealId);
        } catch (DaoException | IllegalArgumentException e) {
            logger.log(Level.ERROR, "Meal cannot be added to menu:", e);
            throw new ServiceException("Meal cannot be added to menu:", e);
        }
    }

    @Override
    public void deleteMealFromMenu(String menuIdStr, String mealIdStr) throws ServiceException {
        try {
            long menuId = Long.parseLong(menuIdStr);
            long mealId = Long.parseLong(mealIdStr);
            menuDao.deleteMealFromMenu(menuId, mealId);
        } catch (DaoException | IllegalArgumentException e) {
            logger.log(Level.ERROR, "Meal cannot be deleted from menu:", e);
            throw new ServiceException("Meal cannot be deleted from menu:", e);
        }
    }

    @Override
    public List<Meal> findMealsByType(String mealType) throws ServiceException {
        List<Meal> meals = new ArrayList<>();
        try {
            Meal.Type type = Meal.Type.valueOf(mealType.toUpperCase());
            meals = mealDao.findMealsByType(type);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be found by type:", e);
            throw new ServiceException("Meal cannot be found by type:", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Enum type has no constant with the specified name:", e);
        }
        return meals;
    }

    @Override
    public List<Meal> findAllMeals() throws ServiceException {
        try {
            return mealDao.findAllEntities();
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meals cannot be found:", e);
            throw new ServiceException("Meals cannot be found:", e);
        }
    }

//    @Override
//    public Map<Meal, String> findAllMeals1() throws ServiceException {
//        try {
//            List<Meal> meals = mealDao.findAllEntities();
//            Map<Meal, String> result = new LinkedHashMap<>();
//            for (Meal meal : meals) {
//                byte[] imageBytes = meal.getImage().getBinaryStream().readAllBytes();
//                byte[] encodeBase64 = Base64.getEncoder().encode(imageBytes);
//                String base64DataString = new String(encodeBase64, StandardCharsets.UTF_8);
//                String src = "data:image/jpeg;base64," + base64DataString;
//                result.put(meal, src);
//            }
//            return result;
//        } catch (DaoException e) {
//            logger.log(Level.ERROR, "Meals cannot be found:", e);
//            throw new ServiceException("Meals cannot be found:", e);
//        } catch (SQLException e) {
//            throw new ServiceException("Image InputStream cannot be received. Error accessing BLOB value:", e);
//        } catch (IOException e) {
//            throw new ServiceException("Image bytes cannot be read from InputStream:", e);
//        }
//    }

    @Override
    public List<Menu> findAllMenu() throws ServiceException {
        try {
            List<Menu> menus = menuDao.findAllEntities();
            for (Menu menu : menus) {
                menu.addAll(menuDao.findMealsForMenu(menu.getId()));
            }
            return menus;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menus cannot be found:", e);
            throw new ServiceException("Menus cannot be found:", e);
        }
    }

    @Override
    public Optional<Menu> findMenuById(String menuIdStr) throws ServiceException {                  // how best to implement?
        Optional<Menu> optionalMenu = Optional.empty();
        try {
            long id = Long.parseLong(menuIdStr);
            optionalMenu = menuDao.findEntityById(id);
            if (optionalMenu.isPresent()) {
                Menu menu = optionalMenu.get();
                menu.addAll(menuDao.findMealsForMenu(id));
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menu cannot be found:", e);
            throw new ServiceException("Menu cannot be found:", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Menu cannot be found as menuIdStr does not contain a parsable long:", e);
        }
        return optionalMenu;
    }

    @Override
    public boolean deleteMenuById(String menuIdStr) throws ServiceException {
        boolean result = false;
        try {
            long id = Long.parseLong(menuIdStr);
            if (menuDao.deleteEntityById(id))
                result = true;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Menu cannot be found:", e);
            throw new ServiceException("Menu cannot be found:", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARN, "Menu cannot be deleted as menuIdStr does not contain a parsable long:", e);
        }
        return result;
    }
}