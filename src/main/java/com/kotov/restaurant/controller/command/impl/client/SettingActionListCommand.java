package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;
import static com.kotov.restaurant.controller.command.PagePath.SETTINGS_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class SettingActionListCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService service = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        long userId = user.getId();
        String action = request.getParameter(ACTION);
        try {
            switch (action) {
                case ADDRESS -> {
                    Map<String, String> dataCheckResult = new HashMap<>();
                    dataCheckResult.put(CITY, request.getParameter(CITY));
                    dataCheckResult.put(STREET, request.getParameter(STREET));
                    dataCheckResult.put(BUILDING, request.getParameter(BUILDING));
                    dataCheckResult.put(BUILDING_BLOCK, request.getParameter(BUILDING_BLOCK));
                    dataCheckResult.put(FLAT, request.getParameter(FLAT));
                    dataCheckResult.put(ENTRANCE, request.getParameter(ENTRANCE));
                    dataCheckResult.put(FLOOR, request.getParameter(FLOOR));
                    dataCheckResult.put(INTERCOM_CODE, request.getParameter(INTERCOM_CODE));
                    if (service.addUserAddress(userId, dataCheckResult)) {
                        request.setAttribute(ADDRESS_ADDING_RESULT, Boolean.TRUE);
                    } else {
                        for (String key : dataCheckResult.keySet()) {
                            String validationResult = dataCheckResult.get(key);
                            if (Boolean.parseBoolean(validationResult)) {
                                switch (key) {
                                    case CITY -> request.setAttribute(VALID_CITY, request.getParameter(CITY));
                                    case STREET -> request.setAttribute(VALID_STREET, request.getParameter(STREET));
                                    case BUILDING -> request.setAttribute(VALID_BUILDING, request.getParameter(BUILDING));
                                    case BUILDING_BLOCK -> request.setAttribute(VALID_BUILDING_BLOCK, request.getParameter(BUILDING_BLOCK));
                                    case FLAT -> request.setAttribute(VALID_FLAT, request.getParameter(FLAT));
                                    case ENTRANCE -> request.setAttribute(VALID_ENTRANCE, request.getParameter(ENTRANCE));
                                    case FLOOR -> request.setAttribute(VALID_FLOOR, request.getParameter(FLOOR));
                                    case INTERCOM_CODE -> request.setAttribute(VALID_INTERCOM_CODE, request.getParameter(INTERCOM_CODE));
                                }
                            } else {
                                switch (validationResult) {
                                    case INVALID_CITY -> request.setAttribute(INVALID_CITY, Boolean.TRUE);
                                    case INVALID_STREET -> request.setAttribute(INVALID_STREET, Boolean.TRUE);
                                    case INVALID_BUILDING -> request.setAttribute(INVALID_BUILDING, Boolean.TRUE);
                                    case INVALID_BUILDING_BLOCK -> request.setAttribute(INVALID_BUILDING_BLOCK, Boolean.TRUE);
                                    case INVALID_FLAT -> request.setAttribute(INVALID_FLAT, Boolean.TRUE);
                                    case INVALID_ENTRANCE -> request.setAttribute(INVALID_ENTRANCE, Boolean.TRUE);
                                    case INVALID_FLOOR -> request.setAttribute(INVALID_FLOOR, Boolean.TRUE);
                                    case INVALID_INTERCOM_CODE -> request.setAttribute(INVALID_INTERCOM_CODE, Boolean.TRUE);
                                }
                            }
                        }
                    }
                }
                case DISCOUNT_CARD -> {
                    String cardNumber = request.getParameter(CARD_NUMBER);
                    if (service.addDiscountCardToUser(userId, cardNumber)) {
                        request.setAttribute(DISCOUNT_CARD_ADDING_RESULT, Boolean.TRUE);
                    } else {
                        request.setAttribute(DISCOUNT_CARD_ADDING_RESULT, Boolean.FALSE);
                    }
                }
                case PERSONAL_DATA -> {
                    Map<String, String> dataCheckResult = new HashMap<>();
                    dataCheckResult.put(FIRST_NAME, request.getParameter(FIRST_NAME));
                    dataCheckResult.put(PATRONYMIC, request.getParameter(PATRONYMIC));
                    dataCheckResult.put(LAST_NAME, request.getParameter(LAST_NAME));
                    dataCheckResult.put(MOBILE_NUMBER, request.getParameter(MOBILE_NUMBER));
                    dataCheckResult.put(EMAIL, request.getParameter(EMAIL));
                    if (service.changeUserPersonalData(userId, dataCheckResult)) {
                        request.setAttribute(PERSONAL_DATA_CHANGING_RESULT, Boolean.TRUE);
                    } else {
                        for (String key : dataCheckResult.keySet()) {
                            String validationResult = dataCheckResult.get(key);
                            if (Boolean.parseBoolean(validationResult)) {
                                switch (key) {
                                    case FIRST_NAME -> request.setAttribute(VALID_FIRST_NAME, request.getParameter(FIRST_NAME));
                                    case PATRONYMIC -> request.setAttribute(VALID_PATRONYMIC, request.getParameter(PATRONYMIC));
                                    case LAST_NAME -> request.setAttribute(VALID_LAST_NAME, request.getParameter(LAST_NAME));
                                    case MOBILE_NUMBER -> request.setAttribute(VALID_MOBILE_NUMBER, request.getParameter(MOBILE_NUMBER));
                                    case EMAIL -> request.setAttribute(VALID_EMAIL, request.getParameter(EMAIL));
                                }
                            } else {
                                switch (validationResult) {
                                    case INVALID_FIRST_NAME -> request.setAttribute(INVALID_FIRST_NAME, Boolean.TRUE);
                                    case INVALID_PATRONYMIC -> request.setAttribute(INVALID_PATRONYMIC, Boolean.TRUE);
                                    case INVALID_LAST_NAME -> request.setAttribute(INVALID_LAST_NAME, Boolean.TRUE);
                                    case INVALID_MOBILE_NUMBER -> request.setAttribute(INVALID_MOBILE_NUMBER, INVALID_MESSAGE);
                                    case NOT_UNIQUE_MOBILE_NUMBER_RESULT -> request.setAttribute(INVALID_MOBILE_NUMBER, NOT_UNIQUE_MESSAGE);
                                    case INVALID_EMAIL -> request.setAttribute(INVALID_EMAIL, INVALID_MESSAGE);
                                    case NOT_UNIQUE_EMAIL_RESULT -> request.setAttribute(INVALID_EMAIL, NOT_UNIQUE_MESSAGE);
                                }
                            }
                        }
                    }
                }
                case NEW_PASSWORD -> {
                    Map<String, String> dataCheckResult = new HashMap<>();
                    dataCheckResult.put(OLD_PASSWORD, request.getParameter(OLD_PASSWORD));
                    dataCheckResult.put(NEW_PASSWORD, request.getParameter(NEW_PASSWORD));
                    dataCheckResult.put(CONFIRM_PASSWORD, request.getParameter(CONFIRM_PASSWORD));

                    if (service.changeAccountPassword(userId, dataCheckResult)) {
                        request.setAttribute(PASSWORD_CHANGING_RESULT, Boolean.TRUE);
                    } else {
                        for (String key : dataCheckResult.keySet()) {
                            String validationResult = dataCheckResult.get(key);
                            if (!Boolean.parseBoolean(validationResult)) {
                                switch (validationResult) {
                                    case INCORRECT_PASSWORD -> request.setAttribute(PASSWORD_CHANGING_RESULT, INCORRECT_MESSAGE);
                                    case INVALID_PASSPORT -> request.setAttribute(PASSWORD_CHANGING_RESULT, INVALID_MESSAGE);
                                    case PASSWORD_MISMATCH -> request.setAttribute(PASSWORD_CHANGING_RESULT, PASSWORD_MISMATCH);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException("Command cannot be completed:", e);
        }
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        router.setPagePath(SETTINGS_PAGE);
        return router;
    }
}