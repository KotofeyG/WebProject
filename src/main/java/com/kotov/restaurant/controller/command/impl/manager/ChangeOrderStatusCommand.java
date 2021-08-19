package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import static com.kotov.restaurant.model.entity.Order.Status.*;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.ORDER_CONFIRMATION_PAGE;

public class ChangeOrderStatusCommand implements Command {
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String orderId = request.getParameter(SELECTED);
        String action = request.getParameter(ACTION);
        try {
            boolean result = switch (action) {
                case APPROVE -> orderService.updateOrderStatus(orderId, APPROVED);
                case REJECT -> orderService.updateOrderStatus(orderId, REJECTED);
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + action);
                    yield false;
                }
            };
            request.setAttribute(ACTION_RESULT, result);
            ManagerCommandsOverallControl.setActiveOrdersToAttribute(request);
            router.setPagePath(ORDER_CONFIRMATION_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to change order status. Order id is " + orderId + ", action is " + action, e);
            throw new CommandException("Impossible to change order status. Order id is " + orderId + ", action is " + action, e);
        }
    }
}