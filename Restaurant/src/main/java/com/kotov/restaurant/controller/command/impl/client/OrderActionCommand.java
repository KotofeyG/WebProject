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

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class OrderActionCommand implements Command {
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        StringBuilder paramPagePath = new StringBuilder();
        User user = (User) session.getAttribute(AttributeName.SESSION_USER);
        String action = request.getParameter(ACTION);
        String orderId = request.getParameter(SELECTED);
        try {
            action = action != null ? action : EMPTY;
            boolean actionResult = switch (action) {
                case REJECT -> orderService.updateOrderStatus(orderId, Order.Status.REJECTED, user.getRole());
                case PAY -> orderService.updateOrderStatus(orderId, Order.Status.PAID, user.getRole());
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + action);
                    yield false;
                }
            };
            paramPagePath.append(ORDER_PAGE).append(PARAMETERS_START).append(ACTION_RESULT).append(EQUALS)
                    .append(actionResult);
            List<Order> orders = orderService.findOrdersByUserId(user.getId());
            session.setAttribute(ORDER_LIST, orders);
            router.setPagePath(paramPagePath.toString());
            if (actionResult) {
                router.setRouterType(REDIRECT);
            }
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to do action with order:", e);
            throw new CommandException("Impossible to do action with order:", e);
        }
    }
}