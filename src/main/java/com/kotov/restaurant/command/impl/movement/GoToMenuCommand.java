package com.kotov.restaurant.command.impl.movement;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.command.PagePath.MENU_PAGE;

public class GoToMenuCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        router.setPagePath(MENU_PAGE);
        return router;
    }
}