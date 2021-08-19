package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.ORDER_CONFIRMATION_PAGE;

public class ShowActiveOrdersCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ManagerCommandsOverallControl.setActiveOrdersToAttribute(request);
        router.setPagePath(ORDER_CONFIRMATION_PAGE);
        return router;
    }
}