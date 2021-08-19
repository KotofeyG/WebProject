package com.kotov.restaurant.controller.command.impl.admin;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.model.entity.User.Status.ACTIVE;
import static com.kotov.restaurant.model.entity.User.Status.BLOCKED;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.USER_MANAGEMENT_PAGE;

public class UserListActionCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String action = request.getParameter(ACTION);
        String[] userIdArray = request.getParameterValues(SELECTED);
        try {
            Boolean result = switch (action) {
                case BLOCK -> userService.updateUserStatusesById(BLOCKED, userIdArray);
                case UNBLOCK -> userService.updateUserStatusesById(ACTIVE, userIdArray);
                case DELETE -> userService.deleteUsersById(userIdArray);
                default -> Boolean.FALSE;
            };
            request.setAttribute(USER_ACTION_RESULT, result);
            List<User> users = userService.findAllUsers();
            request.setAttribute(USER_LIST, users);
            router.setPagePath(USER_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Action with user cannot be completed:", e);
            throw new CommandException("Action with user cannot be completed:", e);
        }
    }
}