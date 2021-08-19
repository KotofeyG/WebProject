package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.MENU_CREATION_PAGE;

public class ShowMenuCreationInfoCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ManagerCommandsOverallControl.setMenuCreationInfo(request);
        router.setPagePath(MENU_CREATION_PAGE);
        return router;
    }
}