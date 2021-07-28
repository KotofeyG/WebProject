package com.kotov.restaurant.controller.command.impl.user;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.NON_EXISTENT_COMMAND_PAGE;

public class DefaultCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(NON_EXISTENT_COMMAND_PAGE);
        return router;
    }
}