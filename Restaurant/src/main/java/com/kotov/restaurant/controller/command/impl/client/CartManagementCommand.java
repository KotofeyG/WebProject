package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.CART_PAGE;

public class CartManagementCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        UserCommandsOverallControl.setCartInfoToAttribute(request);
        router.setPagePath(CART_PAGE);
        return router;
    }
}