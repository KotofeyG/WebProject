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
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.*;

import static com.kotov.restaurant.model.entity.Order.*;
import static com.kotov.restaurant.model.entity.Order.Status.*;
import static com.kotov.restaurant.model.entity.User.Role.CLIENT;
import static com.kotov.restaurant.model.entity.User.Role.MANAGER;

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
        boolean result;
        try {
            long addressId = Long.parseLong(addressIdStr);
            Optional<Address> addressOptional = userDao.findAddressById(addressId);
            if (addressOptional.isPresent()) {
                Address address = addressOptional.get();
                result = insertOrder(orderedMeals, userId, address, time, paymentType);
            } else {
                result = false;
                logger.log(Level.WARN, "Address with id " + addressId + " doesn't exist");
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
    public boolean insertOrder(Map<String, String> orderedMeals, long userId, Map<String, String> addressDataCheckResult, String time, String paymentType) throws ServiceException {
        boolean result;
        UserServiceImpl service =(UserServiceImpl) ServiceProvider.getInstance().getUserService();
        Optional<Address> addressOptional = service.createAddress(addressDataCheckResult);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            try {
                long addressId = userDao.insertAddress(address);
                address.setId(addressId);
                result = insertOrder(orderedMeals, userId, address, time, paymentType);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Order cannot be added into database:", e);
                throw new ServiceException("Order cannot be added into database:", e);
            }
        } else {
            result = false;
            logger.log(Level.WARN, "Address cannot be created as wrong parameters");
        }
        return result;
    }

    private boolean insertOrder(Map<String, String> orderedMeals, long userId, Address address, String time, String paymentType) throws ServiceException {
        boolean result = true;
        Map<Meal, Integer> meals = new HashMap<>();
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
                LocalTime deliveryTime = time != null & !time.isBlank() ? LocalTime.parse(time) : LocalTime.now()
                        .plusHours(DEFAULT_DELIVERY_TIME);
                boolean isCash = Boolean.parseBoolean(paymentType);
                Order order = new Order(meals, userId, address, deliveryTime, isCash);
                result = orderDao.insertNewEntity(order) >= 0;
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
    public List<Order> findOrdersByStatuses(EnumSet<Status> statuses) throws ServiceException {
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
    public boolean updateOrderStatus(String orderIdStr, Status orderStatus, User.Role userRole) throws ServiceException {
        boolean result = false;
        try {
            long orderId = Long.parseLong(orderIdStr);
            Status currentStatus = orderDao.findOrderStatus(orderId);
            if (orderStatus == APPROVED && (currentStatus == IN_PROCESS & userRole == MANAGER)) {
                result = orderDao.updateOrderStatus(orderId, orderStatus);
            } else if (orderStatus == REJECTED) {
                if (currentStatus == IN_PROCESS & (userRole == CLIENT | userRole == MANAGER)) {
                    System.out.println("!!!!!!!!");
                    result = orderDao.updateOrderStatus(orderId, orderStatus);
                } else if (currentStatus == APPROVED & userRole == MANAGER) {
                    result = orderDao.updateOrderStatus(orderId, orderStatus);
                }
            } else if (orderStatus == PAID && currentStatus == APPROVED) {
                if (orderDao.isOrderInCash(orderId) & userRole == MANAGER) {
                    result = orderDao.updateOrderStatus(orderId, orderStatus);
                } else if (!orderDao.isOrderInCash(orderId) & userRole == CLIENT) {
                    result = orderDao.updateOrderStatus(orderId, orderStatus);
                }
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Order status cannot be updated to " + orderStatus, e);
            throw new ServiceException("Order status cannot be updated to " + orderStatus, e);
        } catch (NumberFormatException e) {
            logger.log(Level.WARN, "orderIdStr doesn't contain a parsable long: " + orderIdStr);
        }
        return result;
    }
}