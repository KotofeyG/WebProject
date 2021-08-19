package com.kotov.restaurant.controller.command.impl.admin;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.USER_LIST;
import static com.kotov.restaurant.controller.command.PagePath.USER_MANAGEMENT_PAGE;

public class UserManagementCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        try {
            List<User> users = userService.findAllUsers();
            request.setAttribute(USER_LIST, users);
            router.setPagePath(USER_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Users cannot be found:", e);
            throw new CommandException("Users cannot be found:", e);
        }
    }
}