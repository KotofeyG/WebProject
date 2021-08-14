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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.ACTION_RESULT;
import static com.kotov.restaurant.controller.command.AttributeName.ORDER;
import static com.kotov.restaurant.controller.command.PagePath.ORDERS_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class OrderActionCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.SESSION_USER);
        long userId = user.getId();
        String action = request.getParameter(ACTION);
        String orderIdStr = request.getParameter(SELECTED);
        boolean result;
        try {
            if (PAY.equals(action)) {
                result = orderService.changeOrderStatus(orderIdStr, Order.Status.PAID);
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
            List<Order> orders = orderService.findOrdersByUserId(userId);
            request.setAttribute(ORDER, orders);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to do action with order:", e);
            throw new CommandException("Impossible to do action with order:", e);
        }
        router.setPagePath(ORDERS_PAGE);
        return router;
    }
}