package com.kotov.restaurant.controller.command.impl.movement;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.USER_MANAGEMENT_PAGE;

public class GoToUsersManagementCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(USER_MANAGEMENT_PAGE);
        return router;
    }
}