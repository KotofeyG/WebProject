package com.kotov.restaurant.controller.command.impl.admin;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.model.entity.User.Status.ACTIVE;
import static com.kotov.restaurant.model.entity.User.Status.BLOCKED;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class UserListActionCommand implements Command {
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
        StringBuilder paramPagePath = new StringBuilder();
        String action = request.getParameter(ACTION);
        String[] userIdArray = request.getParameterValues(SELECTED);
        try {
            action = action != null ? action : EMPTY;
            Boolean actionResult = switch (action) {
                case BLOCK -> userService.updateUserStatusesById(BLOCKED, userIdArray);
                case UNBLOCK -> userService.updateUserStatusesById(ACTIVE, userIdArray);
                case DELETE -> userService.deleteUsersById(userIdArray);
                default -> Boolean.FALSE;
            };
            List<User> users = userService.findAllUsers();
            session.setAttribute(USER_LIST, users);
            paramPagePath.append(USER_MANAGEMENT_PAGE).append(PARAMETERS_START).append(USER_ACTION_RESULT)
                    .append(EQUALS).append(actionResult);
            router.setPagePath(paramPagePath.toString());
            if (actionResult) {
                router.setRouterType(REDIRECT);
            }
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Action with user cannot be completed:", e);
            throw new CommandException("Action with user cannot be completed:", e);
        }
    }
}