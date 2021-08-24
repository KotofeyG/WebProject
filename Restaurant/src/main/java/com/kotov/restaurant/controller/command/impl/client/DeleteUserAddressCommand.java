package com.kotov.restaurant.controller.command.impl.client;

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

import static com.kotov.restaurant.controller.command.ParamName.SELECTED;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.SETTINGS_PAGE;

public class DeleteUserAddressCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SESSION_USER);
        String addressId = request.getParameter(SELECTED);
        try {
            request.setAttribute(ADDRESS_DELETION_RESULT, userService.deleteUserAddress(user.getId(), addressId));
            session.setAttribute(ADDRESS_LIST, userService.findUserAddresses(user.getId()));
            router.setPagePath(SETTINGS_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Address cannot be deleted from user:", e);
            throw new CommandException("Address cannot be deleted from user:", e);
        }
    }
}