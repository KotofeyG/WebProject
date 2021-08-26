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
import static com.kotov.restaurant.controller.command.PagePath.MENU_CREATION_PAGE;

public class MenuCreationCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String title = request.getParameter(TITLE);
        String type = request.getParameter(TYPE);
        String[] mealIdArray = request.getParameterValues(SELECTED);
        try {
            request.setAttribute(MENU_CREATION_RESULT, menuService.insertNewMenu(title, type, mealIdArray));
            ManagerCommandsOverallControl.setMenuCreationInfo(request);
            router.setPagePath(MENU_CREATION_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Menu cannot be created:", e);
            throw new CommandException("Menu cannot be created:", e);
        }
    }
}