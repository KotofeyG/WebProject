package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.model.entity.Order.Status.*;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class ChangeOrderStatusCommand implements Command {
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
        User user = (User) session.getAttribute(SESSION_USER);
        String orderId = request.getParameter(SELECTED);
        String action = request.getParameter(ACTION);
        try {
            action = action != null ? action : EMPTY;
            boolean result = switch (action) {
                case APPROVE -> orderService.updateOrderStatus(orderId, APPROVED, user.getRole());
                case REJECT -> orderService.updateOrderStatus(orderId, REJECTED, user.getRole());
                case PAY -> orderService.updateOrderStatus(orderId, PAID, user.getRole());
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + action);
                    yield false;
                }
            };
            ManagerCommandsOverallControl.setActiveOrdersToAttribute(request);
            paramPagePath.append(ORDER_CONFIRMATION_PAGE).append(PARAMETERS_START).append(ACTION_RESULT)
                    .append(EQUALS).append(result);
            router.setPagePath(paramPagePath.toString());
            if (result) {
                router.setRouterType(REDIRECT);
            }
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to change order status. Order id is " + orderId + ", action is " + action, e);
            throw new CommandException("Impossible to change order status. Order id is " + orderId + ", action is " + action, e);
        }
    }
}