package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.util.validator.LocaleValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import static com.kotov.restaurant.controller.command.AttributeName.*;

public class ChangeLocaleCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(CURRENT_PAGE);
        String newLocale = request.getParameter(SESSION_LOCALE);
        logger.log(Level.DEBUG, "Locale parameter is " + newLocale);
        if (LocaleValidator.isLocaleExist(newLocale)) {
            session.setAttribute(SESSION_LOCALE, newLocale);
        } else {
            logger.log(Level.WARN, "Wrong locale parameter: " + newLocale);
        }
        router.setPagePath(currentPage);
        return router;
    }
}