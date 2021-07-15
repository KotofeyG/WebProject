package com.kotov.restaurant.command.impl;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.kotov.restaurant.command.PagePath.*;
import static com.kotov.restaurant.command.ParamName.*;


public class AuthenticationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        UserService service = UserServiceImpl.getInstance();
        Router router = new Router();

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        try {
            Optional<User> userOptional = service.findUserByLoginAndPassword(login, password);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                switch (user.getRole()) {
                    case SUPER_ADMIN -> router.setPagePath(SUPER_ADMIN_MAIN_PAGE);
                    case ADMIN, CLIENT -> router.setPagePath(MAIN_PAGE);
                }
                user.setStatus(User.UserStatus.ONLINE);
                session.setAttribute(USER, user);
            } else {
                router.setPagePath(SIGN_IN);
                request.setAttribute(LOGIN, INVALID_LOGIN_OR_PASSWORD);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}