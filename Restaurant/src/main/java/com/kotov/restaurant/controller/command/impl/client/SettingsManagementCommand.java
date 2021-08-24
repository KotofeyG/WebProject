package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.ADDRESS_LIST;
import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;
import static com.kotov.restaurant.controller.command.PagePath.SETTINGS_PAGE;

public class SettingsManagementCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        try {
            List<Address> addressList = userService.findUserAddresses(user.getId());
            session.setAttribute(ADDRESS_LIST, addressList);
            router.setPagePath(SETTINGS_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Address list cannot be found:", e);
            throw new CommandException("Address list cannot be found:", e);
        }
    }
}