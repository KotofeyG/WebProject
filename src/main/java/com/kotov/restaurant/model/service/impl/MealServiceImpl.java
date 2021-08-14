package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.service.MealService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MealServiceImpl implements MealService {
    private static final Logger logger = LogManager.getLogger();
    private static final MealDao mealDao = DaoProvider.getInstance().getMealDao();

    @Override
    public boolean insertMealToUserCart(long userId, String mealIdStr, String mealQuantityStr) throws ServiceException {
        boolean result = false;
        try {
            long mealId = Long.parseLong(mealIdStr);
            int mealQuantity = Integer.parseInt(mealQuantityStr);
            result = mealDao.insertMealToUserCart(userId, mealId, mealQuantity);
            logger.log(Level.DEBUG, "Method addNewMeal is completed successfully. Result is: " + result);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Meal cannot be added:", e);
            throw new ServiceException("Meal cannot be added:", e);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, "Parameters doesn't contain a parsable values: " + mealIdStr + ", " + mealQuantityStr);
        }
        return result;
    }
}