package com.kotov.restaurant.command.impl;

import com.kotov.restaurant.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.model.service.UserService;
import com.kotov.restaurant.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.command.ParamName.LOGIN;
import static com.kotov.restaurant.command.ParamName.PASSWORD;

public class AuthenticationCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        UserService service = UserServiceImpl.getInstance();
        Router router = new Router();

        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);



        return router;
    }
}