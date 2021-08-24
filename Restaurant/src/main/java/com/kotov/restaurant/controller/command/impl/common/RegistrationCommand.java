package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class RegistrationCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(LOGIN, request.getParameter(LOGIN));
        dataCheckResult.put(PASSWORD, request.getParameter(PASSWORD));
        dataCheckResult.put(CONFIRM_PASSWORD, request.getParameter(CONFIRM_PASSWORD));
        dataCheckResult.put(EMAIL, request.getParameter(EMAIL));
        dataCheckResult.put(MOBILE_NUMBER, request.getParameter(MOBILE_NUMBER));
        dataCheckResult.put(USER_ROLE, request.getParameter(USER_ROLE));
        try {
            boolean registrationResult = userService.registerNewUser(dataCheckResult);
            String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
            if (currentPage.equals(USER_MANAGEMENT_PAGE)) {
                request.setAttribute(USER_LIST, userService.findAllUsers());
                router.setPagePath(USER_MANAGEMENT_PAGE);
            } else {
                if (registrationResult) {
                    router.setPagePath(ACCOUNT_CREATION_DETAILS_PAGE);
                } else {
                    router.setPagePath(REGISTRATION_PAGE);
                }
            }
            if (!registrationResult) {
                for (String key : dataCheckResult.keySet()) {
                    String validationResult = dataCheckResult.get(key);
                    if (Boolean.parseBoolean(validationResult)) {
                        switch (key) {
                            case LOGIN -> request.setAttribute(VALID_LOGIN, request.getParameter(LOGIN));
                            case EMAIL -> request.setAttribute(VALID_EMAIL, request.getParameter(EMAIL));
                            case MOBILE_NUMBER -> request.setAttribute(VALID_MOBILE_NUMBER, request.getParameter(MOBILE_NUMBER));
                        }
                    } else {
                        switch (validationResult) {
                            case INVALID_LOGIN_RESULT -> request.setAttribute(INVALID_LOGIN, INVALID_MESSAGE);
                            case NOT_UNIQUE_LOGIN_RESULT -> request.setAttribute(INVALID_LOGIN, NOT_UNIQUE_MESSAGE);
                            case INVALID_PASSPORT_RESULT -> request.setAttribute(INVALID_PASSPORT, INVALID_MESSAGE);
                            case PASSWORD_MISMATCH -> request.setAttribute(INVALID_PASSPORT, PASSWORD_MISMATCH);
                            case INVALID_EMAIL_RESULT -> request.setAttribute(INVALID_EMAIL, INVALID_MESSAGE);
                            case NOT_UNIQUE_EMAIL_RESULT -> request.setAttribute(INVALID_EMAIL, NOT_UNIQUE_MESSAGE);
                            case INVALID_MOBILE_NUMBER_RESULT -> request.setAttribute(INVALID_MOBILE_NUMBER, INVALID_MESSAGE);
                            case NOT_UNIQUE_MOBILE_NUMBER_RESULT -> request.setAttribute(INVALID_MOBILE_NUMBER, NOT_UNIQUE_MESSAGE);
                        }
                        logger.log(Level.DEBUG, "Validation result: " + key + " - " + validationResult);
                    }
                }
            }
            request.setAttribute(REGISTRATION_RESULT, registrationResult);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "User cannot be registered:", e);
            throw new CommandException("User cannot be registered:", e);
        }
    }
}