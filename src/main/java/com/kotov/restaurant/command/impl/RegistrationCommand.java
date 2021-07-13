package com.kotov.restaurant.command.impl;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.RegistrationData;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.kotov.restaurant.command.PagePath.*;
import static com.kotov.restaurant.command.ParamName.*;

public class RegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        UserService service = UserServiceImpl.getInstance();
        RegistrationData registrationData = new RegistrationData();
        Router router = new Router();
        boolean isNewUserParamsValid = true;

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD);
        String email = request.getParameter(EMAIL);
        String mobileNumber = request.getParameter(MOBILE_NUMBER);

        Map<String, String> dataCheckResult;
        try {
            dataCheckResult = service.checkRegistrationParams(login, password, confirmPassword, email, mobileNumber);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        for (String key : dataCheckResult.keySet()) {
            String validationResult = dataCheckResult.get(key);
            if (Boolean.parseBoolean(validationResult)) {
                switch (key) {
                    case LOGIN -> registrationData.setLogin(login);
                    case EMAIL -> registrationData.setEmail(email);
                    case MOBILE_NUMBER -> registrationData.setMobileNumber(mobileNumber);
                }
            } else {
                switch (validationResult) {
                    case INVALID_LOGIN_MESSAGE -> request.setAttribute(LOGIN_ERROR, INVALID_LOGIN_MESSAGE);
                    case NOT_UNIQUE_LOGIN_MESSAGE -> request.setAttribute(LOGIN_ERROR, NOT_UNIQUE_LOGIN_MESSAGE);
                    case INVALID_PASSPORT_MESSAGE -> request.setAttribute(PASSPORT_ERROR, INVALID_PASSPORT_MESSAGE);
                    case PASSWORD_MISMATCH_MESSAGE -> request.setAttribute(PASSPORT_ERROR, PASSWORD_MISMATCH_MESSAGE);
                    case INVALID_EMAIL_MESSAGE -> request.setAttribute(EMAIL_ERROR, INVALID_EMAIL_MESSAGE);
                    case NOT_UNIQUE_EMAIL_MESSAGE -> request.setAttribute(EMAIL_ERROR, NOT_UNIQUE_EMAIL_MESSAGE);
                    case INVALID_MOBILE_NUMBER_MESSAGE -> request.setAttribute(MOBILE_NUMBER_ERROR, INVALID_MOBILE_NUMBER_MESSAGE);
                    case NOT_UNIQUE_MOBILE_NUMBER_MESSAGE -> request.setAttribute(MOBILE_NUMBER_ERROR, NOT_UNIQUE_MOBILE_NUMBER_MESSAGE);
                }
                isNewUserParamsValid = false;
                logger.log(Level.DEBUG, key + " - " + validationResult);
            }
        }

        if (isNewUserParamsValid) {
            try {
                service.registerNewUser(registrationData, password);
                router.setPagePath(ACCOUNT_CREATION_DETAILS);                                                           // to do ACCOUNT_CREATION_DETAILS
            } catch (ServiceException e) {
                throw new CommandException(e);
            }
        } else {
            router.setPagePath(REGISTRATION_PAGE);                                                                      // to do REGISTRATION_PAGE
            request.setAttribute(REGISTRATION_DATA, registrationData);
            logger.log(Level.DEBUG, registrationData);
        }

        return router;
    }
}