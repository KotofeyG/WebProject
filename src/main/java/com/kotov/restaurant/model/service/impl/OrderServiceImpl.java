package com.kotov.restaurant.model.service.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.dao.DaoProvider;
import com.kotov.restaurant.model.dao.MealDao;
import com.kotov.restaurant.model.dao.OrderDao;
import com.kotov.restaurant.model.dao.UserDao;
import com.kotov.restaurant.model.entity.AbstractEntity;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.service.OrderService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger();
    private static final long DEFAULT_DELIVERY_TIME = 1;
    private static final UserDao userDao = DaoProvider.getInstance().getUserDao();
    private static final MealDao mealDao = DaoProvider.getInstance().getMealDao();
    private static final OrderDao orderDao = DaoProvider.getInstance().getOrderDao();

    @Override
    public Optional<Order> findOrderById(String orderIdStr) {
        return Optional.empty(); //to do
    }

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
            logger.log(Level.ERROR, "Order cannot be found by user id in database:", e);
            throw new ServiceException("Order cannot be found by user id in database:", e);
        }
    }

    @Override
    public boolean addOrder(Map<String, String> orderedMealsStr, long userId, String addressIdStr, String time, String paymentType) throws ServiceException {
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
            long addressId = Long.parseLong(addressIdStr);
            Address address = userDao.findAddressById(addressId).get();
            LocalTime deliveryTime;
            if (time != null) {
                deliveryTime = LocalTime.parse(time);
            } else {
                deliveryTime = LocalTime.now().plusHours(DEFAULT_DELIVERY_TIME);
            }
            boolean isCash = Boolean.parseBoolean(paymentType);

            Order order = new Order(orderedMeals, userId, address, deliveryTime, isCash);
            long orderId = orderDao.insertNewEntity(order);

            List<Long> mealsId = orderedMeals.keySet().stream().map(AbstractEntity::getId).toList();
            userDao.deleteMealsFromCartByUserId(userId, mealsId);
            return orderDao.addMealsToOrder(orderId, orderedMeals);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order cannot be added into database:", e);
            throw new ServiceException("Order cannot be added into database:", e);
        }
    }

    @Override
    public List<Order> findOrdersByStatus(Order.Status status) throws ServiceException {
        try {
            List<Order> orders = orderDao.findOrdersByStatus(status);
            for (Order order : orders) {
                Map<Meal, Integer> meals = orderDao.findMealsForOrder(order.getId());
                order.setMeals(meals);
            }
            return orders;
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order cannot be found by status in database:", e);
            throw new ServiceException("Order cannot be found by status in database:", e);
        }
    }

    @Override
    public boolean changeOrderStatus(String orderIdStr, Order.Status status) throws ServiceException {
        boolean result = false;
        try {
            long orderId = Long.parseLong(orderIdStr);
            result = orderDao.changeOrderStatus(orderId, status);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order status cannot be changed to " + status, e);
            throw new ServiceException("Order status cannot be changed to " + status, e);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, "orderIdStr doesn't contain a parsable long: " + orderIdStr);
        }
        return result;
    }
}