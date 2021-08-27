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

/**
 * This command used by {@link User} for changing password from their cabinet
 */
public class ChangeUserPassword implements Command {
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
        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(OLD_PASSWORD, request.getParameter(OLD_PASSWORD));
        dataCheckResult.put(NEW_PASSWORD, request.getParameter(NEW_PASSWORD));
        dataCheckResult.put(CONFIRM_PASSWORD, request.getParameter(CONFIRM_PASSWORD));
        try {
            boolean actionResult = userService.updateAccountPassword(user.getId(), dataCheckResult);
            if (actionResult) {
                request.setAttribute(PASSWORD_CHANGE_RESULT, actionResult);
            } else {
                for (String key : dataCheckResult.keySet()) {
                    String validationResult = dataCheckResult.get(key);
                    if (!Boolean.parseBoolean(validationResult)) {
                        switch (validationResult) {
                            case INCORRECT_PASSWORD -> request.setAttribute(PASSWORD_CHANGE_RESULT, INCORRECT_MESSAGE);
                            case INVALID_PASSPORT -> request.setAttribute(PASSWORD_CHANGE_RESULT, INVALID_MESSAGE);
                            case PASSWORD_MISMATCH -> request.setAttribute(PASSWORD_CHANGE_RESULT, PASSWORD_MISMATCH);
                        }
                    }
                }
            }
            List<Address> addressList = userService.findUserAddresses(user.getId());
            session.setAttribute(ADDRESS_LIST, addressList);
            router.setPagePath(SETTINGS_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "User password cannot be changed:", e);
            throw new CommandException("User password cannot be changed:", e);
        }
    }
}