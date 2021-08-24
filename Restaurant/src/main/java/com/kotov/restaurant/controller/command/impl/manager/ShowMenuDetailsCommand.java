package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public class ShowMenuDetailsCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = ManagerCommandsOverallControl.setModifiedMenuToAttribute(request);
        return router;
    }
}