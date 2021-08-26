package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.REGISTRATION_PAGE;

public class GoToRegistrationCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(REGISTRATION_PAGE);
        return router;
    }
}