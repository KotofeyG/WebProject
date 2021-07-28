package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class AddNewMenuCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();

        String title = request.getParameter(TITLE);
        String type = request.getParameter(TYPE);
        String[] mealsIdParams = request.getParameterValues(SELECTED);

        try {
            if (service.addNewMenu(title, type, mealsIdParams)) {
                request.setAttribute(MENU_CREATION_DATA, VALID);
            } else {
                request.setAttribute(MENU_CREATION_DATA, NOT_UNIQUE);
            }
            request.setAttribute(ALL_MENUS, service.findAllMenu());
        } catch (ServiceException e) {
            throw new CommandException("Command cannot be completed:", e);
        }

        router.setPagePath(MENU_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, " New menu was completed successfully");
        return router;
    }
}