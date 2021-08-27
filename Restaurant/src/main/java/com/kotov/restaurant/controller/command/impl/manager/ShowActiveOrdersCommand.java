package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.model.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.ORDER_CONFIRMATION_PAGE;

/**
 * Show active orders command
 * Used by managers to show active {@link Order}
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class ShowActiveOrdersCommand implements Command {

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ManagerCommandsOverallControl.setActiveOrdersToAttribute(request);
        router.setPagePath(ORDER_CONFIRMATION_PAGE);
        return router;
    }
}