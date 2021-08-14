package com.kotov.restaurant.controller.command;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Command {
    Logger logger = LogManager.getLogger();
    String PAGE_NUMBER_PARAMETER = "page";

    Router execute(HttpServletRequest request) throws CommandException;

    default int getPage(HttpServletRequest request) {
        int page = 1;
        String stringPage = request.getParameter(PAGE_NUMBER_PARAMETER);
        if (stringPage != null) {
            try {
                page = Integer.parseInt(stringPage);
            } catch (NumberFormatException e) {
                logger.log(Level.WARN, "Cannot parse page number " +  stringPage + ". Use page - 1", e);
            }
        }
        return page;
    }
}