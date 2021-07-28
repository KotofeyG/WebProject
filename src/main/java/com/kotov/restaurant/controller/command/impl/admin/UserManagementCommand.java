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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.PagePath.USER_MANAGEMENT_PAGE;
import static com.kotov.restaurant.controller.command.AttributeName.ALL_USERS;

public class UserManagementCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService service = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        try {
            List<User> users = service.findAllUsers();
            request.setAttribute(ALL_USERS, users);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find all users: ", e);
            throw new CommandException("Impossible to find all users: ", e);
        }
        router.setPagePath(USER_MANAGEMENT_PAGE);
        return router;
    }
}