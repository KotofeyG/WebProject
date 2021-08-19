package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.AttributeName;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.ORDER_LIST;
import static com.kotov.restaurant.controller.command.AttributeName.ACTION_RESULT;
import static com.kotov.restaurant.controller.command.PagePath.ORDER_PAGE;

public class OrderActionCommand implements Command {
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.SESSION_USER);
        String action = request.getParameter(ACTION);
        String orderId = request.getParameter(SELECTED);
        try {
            boolean actionResult = switch (action) {
                case PAY -> orderService.updateOrderStatus(orderId, Order.Status.PAID);
                case REJECT -> orderService.updateOrderStatus(orderId, Order.Status.REJECTED);
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + action);
                    yield false;
                }
            };
            request.setAttribute(ACTION_RESULT, actionResult);
            List<Order> orders = orderService.findOrdersByUserId(user.getId());
            request.setAttribute(ORDER_LIST, orders);
            router.setPagePath(ORDER_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to do action with order:", e);
            throw new CommandException("Impossible to do action with order:", e);
        }
    }
}