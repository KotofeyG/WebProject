package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.AttributeName.WRONG_COMMAND;
import static com.kotov.restaurant.controller.command.PagePath.ERROR_400_PAGE;

public class NonExistentCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        request.setAttribute(WRONG_COMMAND, Boolean.TRUE);
        router.setPagePath(ERROR_400_PAGE);
        return router;
    }
}