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
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    private static final String FIND_ORDER_PAYMENT_TYPE = "SELECT cash FROM orders WHERE id=?";
    private static final String FIND_ORDERS_BY_STATUSES = "SELECT orders.id, status, delivery_time, cash, orders.user_id, created, address.id, city, street, building, block, flat, entrance, floor, intercom_code FROM orders" +
            " JOIN  address ON address.id=address_id " +
            " JOIN city_names ON city_names.id=address.city_id " +
            " JOIN order_statuses ON order_statuses.id=status_id " +
            " WHERE status_id IN ()";
    private static final String INDEX = "?, ";
    private static final int STARTING_INPUT_INDEX = 326;
    private static final String ORDER_BY_ORDER_ID_STATUSES = " ORDER BY order_statuses.id";
    private static final String FIND_ORDER_BY_STATUS = "SELECT order_statuses.status FROM orders" +
            " JOIN order_statuses ON order_statuses.id=status_id WHERE orders.id=?";
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
    private static final String DELETE_MEAL_FROM_CART = "DELETE FROM carts WHERE user_id=? AND meal_id=?";

    @Override
    public Optional<Order> findEntityById(long id) {
        throw new UnsupportedOperationException("findEntityById(long id) method is not supported");
    }

    @Override
    public List<Order> findAllEntities() {
        throw new UnsupportedOperationException("findAllEntities() method is not supported");
    }

    @Override
    public long insertNewEntity(Order order) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement orderStatement = connection.prepareStatement(INSERT_NEW_ORDER, RETURN_GENERATED_KEYS);
                 PreparedStatement mealStatement = connection.prepareStatement(ADD_MEALS_TO_ORDER);
                 PreparedStatement cartStatement = connection.prepareStatement(DELETE_MEAL_FROM_CART)) {
                orderStatement.setTime(FIRST_PARAM_INDEX, Time.valueOf(order.getDeliveryTime()));
                orderStatement.setBoolean(SECOND_PARAM_INDEX, order.isCash());
                orderStatement.setTimestamp(THIRD_PARAM_INDEX, Timestamp.valueOf(order.getCreated()));
                orderStatement.setLong(FOURTH_PARAM_INDEX, order.getUserId());
                orderStatement.setLong(FIFTH_PARAM_INDEX, order.getStatus().ordinal() + 1);
                orderStatement.setLong(SIXTH_PARAM_INDEX, order.getAddress().getId());
                orderStatement.executeUpdate();
                ResultSet resultSet = orderStatement.getGeneratedKeys();
                long orderId = 0;
                if (resultSet.next()) {
                    orderId = resultSet.getLong(FIRST_PARAM_INDEX);
                    for (Map.Entry<Meal, Integer> entry : order.getMeals().entrySet()) {
                        Meal meal = entry.getKey();
                        int quantity = entry.getValue();
                        mealStatement.setLong(FIRST_PARAM_INDEX, orderId);
                        mealStatement.setLong(SECOND_PARAM_INDEX, meal.getId());
                        mealStatement.setInt(THIRD_PARAM_INDEX, quantity);
                        mealStatement.addBatch();
                    }
                    mealStatement.executeBatch();
                    for (Meal meal : order.getMeals().keySet()) {
                        cartStatement.setLong(FIRST_PARAM_INDEX, order.getUserId());
                        cartStatement.setLong(SECOND_PARAM_INDEX, meal.getId());
                        cartStatement.addBatch();
                    }
                    cartStatement.executeBatch();
                }
                connection.commit();
                logger.log(Level.INFO, "insertNewEntity method was completed successfully. Order with id " + orderId + " was added");
                return orderId;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                logger.log(Level.ERROR, "Change cancellation error in the current transaction:", throwables);
            }
            logger.log(Level.ERROR, "Impossible to insert order into database. Database access error:", e);
            throw new DaoException("Impossible to insert order into database. Database access error:", e);
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
    public boolean deleteEntityById(long id) {
        throw new UnsupportedOperationException("deleteEntityById(long id) method is not supported");
    }

    @Override
    public boolean deleteEntitiesById(List<Long> idList) {
        throw new UnsupportedOperationException("deleteEntitiesById(List<Long> idList) method is not supported");
    }

    @Override
    public List<Order> findOrdersByStatuses(EnumSet<Order.Status> statuses) throws DaoException {
        StringBuilder resultSqlQuery = new StringBuilder(FIND_ORDERS_BY_STATUSES);
        resultSqlQuery.insert(STARTING_INPUT_INDEX, INDEX.repeat(statuses.size())).delete(resultSqlQuery.length() - 3, resultSqlQuery.length()- 1)
                .append(ORDER_BY_ORDER_ID_STATUSES);
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(resultSqlQuery.toString())) {
            Order.Status [] statusArray = new Order.Status[statuses.size()];
            statuses.toArray(statusArray);
            for (int i = 0; i < statusArray.length; i++) {
                statement.setInt(i + 1, statusArray[i].ordinal() + 1);
            }
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
            logger.log(Level.DEBUG, !orders.isEmpty() ? orders.size() + " orders were found by statuses"
                    : "There weren't found any orders by statuses");
            return orders;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find orders by statuses into database. Database access error:", e);
            throw new DaoException("Impossible to find orders by statuses into database. Database access error:", e);
        }
    }

    @Override
    public boolean isOrderInCash(long orderId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDER_PAYMENT_TYPE)) {
            statement.setLong(FIRST_PARAM_INDEX, orderId);
            ResultSet resultSet = statement.executeQuery();
            boolean paymentType = false;
            if (resultSet.next()) {
                paymentType = resultSet.getBoolean(ORDER_CASH);
            }
            logger.log(Level.DEBUG, paymentType ? "Order with id " + orderId + " has cash payment type"
                    : "Order with id " + orderId + " has cashless payment type");
            return paymentType;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to check order payment type into database. Database access error:", e);
            throw new DaoException("Impossible to check order payment type into database. Database access error:", e);
        }
    }

    @Override
    public List<Order> findOrdersByUserId(long userId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
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
            logger.log(Level.DEBUG, !orders.isEmpty() ? orders.size() + " orders were found with user id " + userId
                    : "There weren't any orders with user id " + userId);
            return orders;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find orders with user id " + userId + " into database. Database access error:", e);
            throw new DaoException("Impossible to find orders with user id " + userId + " into database. Database access error:", e);
        }
    }

    @Override
    public Map<Meal, Integer> findMealsForOrder(long orderId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_MEALS_FOR_ORDERS)) {
            statement.setLong(FIRST_PARAM_INDEX, orderId);
            ResultSet resultSet = statement.executeQuery();
            Map<Meal, Integer> meals = new LinkedHashMap<>();
            while (resultSet.next()) {
                Meal meal = MealCreator.create(resultSet);
                Integer quantity = resultSet.getInt(ORDER_QUANTITY);
                meals.put(meal, quantity);
            }
            logger.log(Level.DEBUG, !meals.isEmpty() ? meals.size() + " type of meals were found for order with id " + orderId
                    : "There weren't any meals for order with id " + orderId);
            return meals;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find meals for order with id " + orderId + " into database. Database access error:", e);
            throw new DaoException("Impossible to find meals for order with id " + orderId + " into database. Database access error:", e);
        }
    }

    @Override
    public Optional<Order.Status> findOrderStatus(long orderId) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDER_BY_STATUS)) {
            statement.setLong(FIRST_PARAM_INDEX, orderId);
            ResultSet resultSet = statement.executeQuery();
            Optional<Order.Status> optionalStatus = Optional.empty();
            if (resultSet.next()) {
                Order.Status status = Order.Status.valueOf(resultSet.getString(ORDER_STATUS).toUpperCase());
                optionalStatus = Optional.of(status);
            }
            logger.log(Level.DEBUG, optionalStatus.map(status -> "Order with id " + orderId + " has status " + status)
                    .orElseGet(() -> "Order with id " + orderId + " doesn't have any status"));
            return optionalStatus;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Impossible to find order by status. Database access error:", e);
            throw new DaoException("Impossible to find order by status. Database access error:", e);
        }
    }

    @Override
    public boolean updateOrderStatus(long orderId, Order.Status status) throws DaoException {
        try (Connection connection = connectionPool.getConnection();
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