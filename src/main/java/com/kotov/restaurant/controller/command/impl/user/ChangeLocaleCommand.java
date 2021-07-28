package com.kotov.restaurant.controller.command.impl.user;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class ChangeLocaleCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String newLocale = request.getParameter(LOCALE);
        HttpSession session = request.getSession();
        session.setAttribute(LOCALE, newLocale);
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        router.setPagePath(currentPage);
        return router;
    }
}