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

import static com.kotov.restaurant.controller.command.AttributeName.ORDER_LIST;
import static com.kotov.restaurant.controller.command.PagePath.ORDER_PAGE;

public class ShowUserOrdersCommand implements Command {
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.SESSION_USER);
        try {
            List<Order> orders = orderService.findOrdersByUserId(user.getId());
            request.setAttribute(ORDER_LIST, orders);
            router.setPagePath(ORDER_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "User orders cannot be found:", e);
            throw new CommandException("User orders cannot be found:", e);
        }
    }
}