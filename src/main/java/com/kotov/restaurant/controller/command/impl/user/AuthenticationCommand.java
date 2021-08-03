package com.kotov.restaurant.controller.command.impl.user;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class AuthenticationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService service = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        try {
            Optional<User> userOptional = service.findUserByLoginAndPassword(login, password);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                session.setAttribute(USER_ATTR, user);                           // set user status
                request.setAttribute(AUTHORIZATION_DATA, Boolean.TRUE);
            } else {
                request.setAttribute(AUTHORIZATION_DATA, Boolean.FALSE);
            }
            router.setPagePath(MAIN_PAGE);                                   // change modal plugin, get attr in jsp
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to authentication user: ", e);
            throw new CommandException("Impossible to authentication user: ", e);
        }
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}