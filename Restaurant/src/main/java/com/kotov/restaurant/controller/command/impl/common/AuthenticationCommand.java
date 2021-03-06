package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.Optional;

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.controller.command.ParamName.LOGIN;
import static com.kotov.restaurant.controller.command.ParamName.PASSWORD;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MAIN_PAGE;

/**
 * {@link User} authentication command. Check input by user data in data base.
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class AuthenticationCommand implements Command {
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
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        try {
            Optional<User> userOptional = userService.findUserByLoginAndPassword(login, password);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole() == User.Role.ADMIN || user.getStatus() != User.Status.BLOCKED) {
                    session.setAttribute(SESSION_USER, user);
                    request.setAttribute(AUTHENTICATION_RESULT, Boolean.TRUE);
                } else {
                    router.setErrorCode(HttpServletResponse.SC_FORBIDDEN);
                    router.setErrorMessage(ACCOUNT_BLOCKAGE_MESSAGE);
                }
            } else {
                request.setAttribute(AUTHENTICATION_RESULT, Boolean.FALSE);
            }
            router.setPagePath(MAIN_PAGE);
            router.setRouterType(REDIRECT);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Authentication cannot be completed:", e);
            throw new CommandException("Authentication cannot be completed:", e);
        }
    }
}