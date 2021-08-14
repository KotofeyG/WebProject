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

import static com.kotov.restaurant.controller.command.AttributeName.ORDER;
import static com.kotov.restaurant.controller.command.PagePath.ORDERS_PAGE;

public class CheckOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.SESSION_USER);
        long userId = user.getId();
        try {
            List<Order> orders = orderService.findOrdersByUserId(userId);
            request.setAttribute(ORDER, orders);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find user orders:", e);
            throw new CommandException("Impossible to find user orders:", e);
        }
        router.setPagePath(ORDERS_PAGE);
        return router;
    }
}