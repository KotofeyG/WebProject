package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.ORDER_CONFIRMATION_PAGE;

public class ChangeOrderStatusCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String orderIdStr = request.getParameter(SELECTED);
        String action = request.getParameter(ACTION);

        boolean result;
        try {
            if (APPROVE.equals(action)) {
                result = orderService.changeOrderStatus(orderIdStr, Order.Status.APPROVED);
            } else if (REJECT.equals(action)) {
                result = orderService.changeOrderStatus(orderIdStr, Order.Status.REJECTED);
            } else {
                result = false;
            }
            if (result) {
                request.setAttribute(ACTION_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(ACTION_RESULT, Boolean.FALSE);
            }
            Map<Order, User> activeOrders = new LinkedHashMap<>();                           // move to utility class?
            List<Order> activeOrderList = orderService.findOrdersByStatus(Order.Status.IN_PROCESS);    // to use EnumSet?
            activeOrderList.addAll(orderService.findOrdersByStatus(Order.Status.APPROVED));
            for (Order order : activeOrderList) {
                Optional<User> optionalUser = userService.findUserById(order.getUserId());    // is it right?
                if (optionalUser.isPresent()) {
                    User client = optionalUser.get();
                    activeOrders.put(order, client);
                } else {
                    logger.log(Level.WARN, "User with id " + order.getUserId() + " doesn't exist");
                }
            }
            if (activeOrders.size() == 0) {
                request.setAttribute(ZERO_NUMBER_OF_ORDERS, Boolean.TRUE);
            }
            request.setAttribute(ORDER, activeOrders);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to change order status. Order id is " + orderIdStr + ", action is " + action, e);
            throw new CommandException("Impossible to change order status. Order id is " + orderIdStr + ", action is " + action, e);
        }
        router.setPagePath(ORDER_CONFIRMATION_PAGE);
        return router;
    }
}