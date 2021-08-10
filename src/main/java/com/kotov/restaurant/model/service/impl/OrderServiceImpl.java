package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.dao.OrderDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.service.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final MealDao mealDao = DaoProvider.getInstance().getMealDao();
    private static final OrderDao orderDao = DaoProvider.getInstance().getOrderDao();

    @Override
    public boolean addOrder(long userId, Map<String, String> orderedMealsStr) throws ServiceException {
        Map<Meal, Integer> orderedMeals = new LinkedHashMap<>();
        try {
            for (Map.Entry<String, String> entry : orderedMealsStr.entrySet()) {
                long mealId = Long.parseLong(entry.getKey());
                Optional<Meal> optionalMeal = mealDao.findEntityById(mealId);
                if (optionalMeal.isPresent()) {
                    Meal meal = optionalMeal.get();
                    Integer mealQuantity = Integer.valueOf(entry.getValue());
                    orderedMeals.put(meal, mealQuantity);
                } else {
                    logger.log(Level.WARN, "Meal with id " + mealId + " doesn't exist");
                }
            }
            Order order = new Order(orderedMeals, LocalDateTime.now(), Order.Status.IN_PROCESS);
            return orderDao.addOrder(userId, order);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order cannot be added:", e);
            throw new ServiceException("Order cannot be added:", e);
        }
    }
}