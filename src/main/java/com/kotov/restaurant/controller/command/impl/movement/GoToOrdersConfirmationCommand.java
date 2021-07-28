package com.kotov.restaurant.controller.command.impl.movement;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.ORDER_CONFIRMATION_PAGE;

public class GoToOrdersConfirmationCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(ORDER_CONFIRMATION_PAGE);
        return router;
    }
}