package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.dao.OrderDao;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.service.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.*;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final long DEFAULT_DELIVERY_TIME = 1;
    private static final UserDao userDao = DaoProvider.getInstance().getUserDao();
    private static final MealDao mealDao = DaoProvider.getInstance().getMealDao();
    private static final OrderDao orderDao = DaoProvider.getInstance().getOrderDao();

    @Override
    public List<Order> findOrdersByUserId(long userId) throws ServiceException {
        try {
            List<Order> orders = orderDao.findOrdersByUserId(userId);
            for (Order order : orders) {
                Map<Meal, Integer> meals = orderDao.findMealsForOrder(order.getId());
                order.setMeals(meals);
            }
            return orders;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order cannot be found by user with id " + userId + " in database:", e);
            throw new ServiceException("Order cannot be found by user with id " + userId + " in database:", e);
        }
    }

    @Override
    public boolean insertOrder(Map<String, String> orderedMeals, long userId, String addressIdStr, String time, String paymentType) throws ServiceException {
        boolean result = true;
        Map<Meal, Integer> meals = new LinkedHashMap<>();
        try {
            for (Map.Entry<String, String> entry : orderedMeals.entrySet()) {
                long mealId = Long.parseLong(entry.getKey());
                Optional<Meal> optionalMeal = mealDao.findEntityById(mealId);
                if (optionalMeal.isPresent()) {
                    Meal meal = optionalMeal.get();
                    Integer mealQuantity = Integer.valueOf(entry.getValue());
                    meals.put(meal, mealQuantity);
                } else {
                    result = false;
                    logger.log(Level.WARN, "Meal with id " + mealId + " doesn't exist");
                }
            }
            if (result) {
                long addressId = Long.parseLong(addressIdStr);
                Optional<Address> addressOptional = userDao.findAddressById(addressId);
                if (addressOptional.isPresent()) {
                    Address address = addressOptional.get();
                    LocalTime deliveryTime = time != null & !time.isBlank() ? LocalTime.parse(time) : LocalTime.now().plusHours(DEFAULT_DELIVERY_TIME);
                    boolean isCash = Boolean.parseBoolean(paymentType);
                    Order order = new Order(meals, userId, address, deliveryTime, isCash);
                    result = orderDao.insertNewEntity(order) >= 0;
                } else {
                    result = false;
                    logger.log(Level.WARN, "Address with id " + addressId + " doesn't exist");
                }
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order cannot be added into database:", e);
            throw new ServiceException("Order cannot be added into database:", e);
        } catch (NumberFormatException e) {
            result = false;
            logger.log(Level.WARN, "String does not contain a parsable value:", e);
        }
        return result;
    }

    @Override
    public List<Order> findOrdersByStatuses(EnumSet<Order.Status> statuses) throws ServiceException {
        try {
            List<Order> orders = orderDao.findOrdersByStatuses(statuses);
            for (Order order : orders) {
                Map<Meal, Integer> meals = orderDao.findMealsForOrder(order.getId());
                order.setMeals(meals);
            }
            return orders;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Orders cannot be found by statuses in database:", e);
            throw new ServiceException("Orders cannot be found by statuses in database:", e);
        }
    }

    @Override
    public boolean updateOrderStatus(String orderIdStr, Order.Status status) throws ServiceException {
        boolean result = false;
        try {
            long orderId = Long.parseLong(orderIdStr);
            result = orderDao.updateOrderStatus(orderId, status);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order status cannot be updated to " + status, e);
            throw new ServiceException("Order status cannot be updated to " + status, e);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, "orderIdStr doesn't contain a parsable long: " + orderIdStr);
        }
        return result;
    }
}