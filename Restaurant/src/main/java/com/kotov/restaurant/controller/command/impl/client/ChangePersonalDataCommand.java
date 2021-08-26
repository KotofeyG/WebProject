package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class ChangePersonalDataCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        String type = request.getParameter(TYPE);
        String value = request.getParameter(VALUE);
        try {
            type = type != null ? type : EMPTY;
            boolean result = switch (type) {
                case FIRST_NAME -> {
                    boolean changeResult = userService.updateFirstNameById(user.getId(), value);
                    request.setAttribute(FIRST_NAME_CHANGE_RESULT, changeResult);
                    yield changeResult;
                }
                case PATRONYMIC -> {
                    boolean changeResult = userService.updatePatronymicById(user.getId(), value);
                    request.setAttribute(PATRONYMIC_CHANGE_RESULT, changeResult);
                    yield changeResult;
                }
                case LAST_NAME -> {
                    boolean changeResult = userService.updateLastNameById(user.getId(), value);
                    request.setAttribute(LAST_NAME_CHANGE_RESULT, changeResult);
                    yield changeResult;
                }
                case MOBILE_NUMBER -> {
                    Map<String, String> dataCheckResult = new HashMap<>();
                    dataCheckResult.put(MOBILE_NUMBER, value);
                    boolean changeResult = userService.updateMobileNumberById(user.getId(), dataCheckResult);
                    if (changeResult) {
                        request.setAttribute(MOBILE_NUMBER_CHANGE_RESULT, changeResult);
                    } else {
                        request.setAttribute(MOBILE_NUMBER_CHANGE_RESULT, dataCheckResult.get(MOBILE_NUMBER));
                    }
                    yield changeResult;
                }
                case EMAIL -> {
                    Map<String, String> dataCheckResult = new HashMap<>();
                    dataCheckResult.put(EMAIL, value);
                    boolean changeResult = userService.updateEmailById(user.getId(), dataCheckResult);
                    if (changeResult) {
                        request.setAttribute(EMAIL_CHANGE_RESULT, changeResult);
                    } else {
                        request.setAttribute(EMAIL_CHANGE_RESULT, dataCheckResult.get(EMAIL));
                    }
                    yield changeResult;
                }
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + type);
                    yield false;
                }
            };
            List<Address> addressList = userService.findUserAddresses(user.getId());
            session.setAttribute(ADDRESS_LIST, addressList);
            request.setAttribute(PERSONAL_DATA_CHANGE_RESULT, result);
            router.setPagePath(SETTINGS_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Personal data cannot be changed:", e);
            throw new CommandException("Personal data cannot be changed:", e);
        }
    }
}