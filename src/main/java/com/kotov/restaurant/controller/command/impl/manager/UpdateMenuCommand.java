package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class UpdateMenuCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router;
        String menuId = request.getParameter(SELECTED_MENU);
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            if (currentPage.equals(MENU_UPDATE_PAGE)) {
                String action = request.getParameter(ACTION);
                String mealId = request.getParameter(SELECTED);
                boolean result = switch (action) {
                    case APPEND -> menuService.addMealToMenu(menuId, mealId);
                    case REMOVE -> menuService.deleteMealFromMenu(menuId, mealId);
                    default -> {
                        logger.log(Level.WARN, "Unexpected value: " + action);
                        yield false;
                    }
                };
                request.setAttribute(ACTION_RESULT, result);
            }
            router = ManagerCommandsOverallControl.setModifiedMenuToAttribute(request);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to update menu with id " + menuId, e);
            throw new CommandException("Impossible to update menu with id " + menuId, e);
        }
    }
}