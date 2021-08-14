package com.kotov.restaurant.model.dao.impl;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.dao.OrderDao;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.kotov.restaurant.model.dao.ColumnName.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger();
    private static final ConnectionPool connection_pool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_ORDERS_BY_STATUS = "SELECT orders.id, delivery_time, cash, orders.user_id, created, address.id, city, street, building, block, flat, entrance, floor, intercom_code FROM orders" +
            " JOIN  address ON address.id=address_id " +
            " JOIN city_names ON city_names.id=address.city_id " +
            " WHERE status_id=?";
    private static final String FIND_ORDERS_BY_USER_ID = "SELECT orders.id, delivery_time, cash, orders.user_id, created, status, address.id, city, street, building, block, flat, entrance, floor, intercom_code FROM orders" +
            " JOIN  address ON address.id=address_id" +
            " JOIN city_names ON city_names.id=address.city_id" +
            " JOIN order_statuses ON order_statuses.id=status_id" +
            " WHERE orders.user_id=?";
    private static final String FIND_MEALS_FOR_ORDERS = "SELECT meals.id, title, image, meal_types.type, price, recipe, created, active, quantity FROM meals" +
            " JOIN meal_types ON meal_types.id=type_id" +
            " JOIN orders_have_meals ON meal_id=meals.id AND order_id=?";
    private static final String INSERT_NEW_ORDER = "INSERT INTO orders (delivery_time, cash, created, user_id, status_id, address_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String ADD_MEALS_TO_ORDER = "INSERT INTO orders_have_meals (order_id, meal_id, quantity) VALUE (?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status_id=? WHERE id=?";

    @Override
    public Optional<Order> findEntityById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAllEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insertNewEntity(Order order) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ORDER, RETURN_GENERATED_KEYS)) {
            statement.setTime(FIRST_PARAM_INDEX, Time.valueOf(order.getDeliveryTime()));
            statement.setBoolean(SECOND_PARAM_INDEX, order.isCash());
            statement.setTimestamp(THIRD_PARAM_INDEX, Timestamp.valueOf(order.getCreated()));
            statement.setLong(FOURTH_PARAM_INDEX, order.getUserId());
            statement.setLong(FIFTH_PARAM_INDEX, order.getStatus().ordinal() + 1);
            statement.setLong(SIXTH_PARAM_INDEX, order.getAddress().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            long orderId = 0;
            if (resultSet.next()) {
                orderId = resultSet.getLong(FIRST_PARAM_INDEX);
            }
            logger.log(Level.INFO, "insertNewEntity method was completed successfully. Order with id " + orderId + " was added");
            return orderId;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to insert order into database. Database access error:", e);
            throw new DaoException("Impossible to insert order into database. Database access error:", e);
        }
    }

    @Override
    public boolean deleteEntityById(long id) {
        return false;
    }

    @Override
    public boolean deleteEntitiesById(List<Long> idList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addMealsToOrder(long orderId, Map<Meal, Integer> orderedMeals) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_MEALS_TO_ORDER)) {
            for (Map.Entry<Meal, Integer> entry : orderedMeals.entrySet()) {
                Meal meal = entry.getKey();
                int quantity = entry.getValue();
                statement.setLong(FIRST_PARAM_INDEX, orderId);
                statement.setLong(SECOND_PARAM_INDEX, meal.getId());
                statement.setInt(THIRD_PARAM_INDEX, quantity);
                statement.addBatch();
            }
            boolean result = statement.executeBatch().length == orderedMeals.size();
            logger.log(Level.DEBUG, result ? orderedMeals.size() + " meals were added to order with id " + orderId
                    : "Meals weren't added to order with id " + orderId);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to add meals to  order with id " + orderId + " into database. Database access error:", e);
            throw new DaoException("Impossible to add meals to  order with id " + orderId + " into database. Database access error:", e);
        }
    }

    @Override
    public List<Order> findOrdersByStatus(Order.Status status) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDERS_BY_STATUS)) {
            statement.setInt(FIRST_PARAM_INDEX, status.ordinal() + 1);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong(ORDER_ID));
                order.setUserId(resultSet.getLong(ORDER_USER_ID));
                order.setAddress(AddressCreator.create(resultSet));
                order.setDeliveryTime(LocalTime.parse(resultSet.getString(DELIVERY_TIME)));
                order.setCash(resultSet.getBoolean(ORDER_CASH));
                order.setCreated(LocalDateTime.parse(resultSet.getString(ORDER_CREATED), FORMATTER));
                order.setStatus(status);
                orders.add(order);
            }
            logger.log(Level.DEBUG, orders.size() != 0 ? orders.size() + " orders were found with status " + status
                    : "There weren't any orders with status " + status);
            return orders;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find orders with status " + status + " into database. Database access error:", e);
            throw new DaoException("Impossible to find orders with status " + status + " into database. Database access error:", e);
        }

    }

    @Override
    public List<Order> findOrdersByUserId(long userId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDERS_BY_USER_ID)) {
            statement.setLong(FIRST_PARAM_INDEX, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong(ORDER_ID));
                order.setUserId(resultSet.getLong(ORDER_USER_ID));
                order.setAddress(AddressCreator.create(resultSet));
                order.setDeliveryTime(LocalTime.parse(resultSet.getString(DELIVERY_TIME)));
                order.setCash(resultSet.getBoolean(ORDER_CASH));
                order.setCreated(LocalDateTime.parse(resultSet.getString(ORDER_CREATED), FORMATTER));
                order.setStatus(Order.Status.valueOf(resultSet.getString(ORDER_STATUS).toUpperCase()));
                orders.add(order);
            }
            logger.log(Level.DEBUG, orders.size() != 0 ? orders.size() + " orders were found with user id " + userId
                    : "There weren't any orders with user id " + userId);
            return orders;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find orders with user id " + userId + " into database. Database access error:", e);
            throw new DaoException("Impossible to find orders with user id " + userId + " into database. Database access error:", e);
        }
    }

    @Override
    public Map<Meal, Integer> findMealsForOrder(long orderId) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEALS_FOR_ORDERS)) {
            statement.setLong(FIRST_PARAM_INDEX, orderId);
            ResultSet resultSet = statement.executeQuery();
            Map<Meal, Integer> meals = new LinkedHashMap<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                Integer quantity = resultSet.getInt(ORDER_QUANTITY);
                meals.put(meal, quantity);
            }
            logger.log(Level.DEBUG, meals.size() != 0 ? meals.size() + " type of meals were found for order with id " + orderId
                    : "There weren't any meals for order with id " + orderId);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for order with id " + orderId + " into database. Database access error:", e);
            throw new DaoException("Impossible to find meals for order with id " + orderId + " into database. Database access error:", e);
        }
    }

    @Override
    public boolean changeOrderStatus(long orderId, Order.Status status) throws DaoException {
        try (Connection connection = connection_pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_STATUS)) {
            statement.setLong(FIRST_PARAM_INDEX, status.ordinal() + 1);
            statement.setLong(SECOND_PARAM_INDEX, orderId);
            boolean result = statement.executeUpdate() == 1;
            logger.log(Level.INFO, result ? "Order status for order with id " + orderId + " was changed to " + status
                    : "Order status for order with id " + orderId + " wasn't changed to " + status);
            return result;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to change order status for order with id " + orderId + " into database. Database access error:", e);
            throw new DaoException("Impossible to change order status for order with id " + orderId + " into database. Database access error:", e);
        }
    }
}