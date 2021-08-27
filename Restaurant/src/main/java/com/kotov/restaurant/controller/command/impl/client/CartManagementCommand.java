package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.model.entity.Meal;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.CART_PAGE;

/**
 * Cart management command.
 * Used by clients for checking {@link Meal}s in their cart.
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class CartManagementCommand implements Command {

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        UserCommandsOverallControl.setCartInfoToAttribute(request);
        router.setPagePath(CART_PAGE);
        return router;
    }
}