package com.kotov.restaurant.controller.command.impl.user;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.kotov.restaurant.controller.command.PagePath.INDEX_PAGE;

public class LogoutCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        session.invalidate();
        router.setPagePath(INDEX_PAGE);
        return router;
    }
}