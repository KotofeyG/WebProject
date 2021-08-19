package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.kotov.restaurant.controller.command.ParamName.FIRST_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.PAGE_NUMBER;

public interface Command {
    Logger logger = LogManager.getLogger();

    Router execute(HttpServletRequest request) throws CommandException;

    default int getPage(HttpServletRequest request) {
        int page = FIRST_PAGE;
        String strPage = request.getParameter(PAGE_NUMBER);
        if (strPage != null) {
            try {
                page = Integer.parseInt(strPage);
            } catch (NumberFormatException e) {
                logger.log(Level.WARN, "Cannot parse page number " +  strPage + ". The first page will be used - 1", e);
            }
        }
        return page;
    }
}