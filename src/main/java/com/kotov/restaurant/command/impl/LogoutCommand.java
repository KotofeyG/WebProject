package com.kotov.restaurant.command.impl;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.command.PagePath;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Router router = new Router();
        session.invalidate();
        router.setPagePath(PagePath.MAIN_PAGE);
        return router;
    }
}