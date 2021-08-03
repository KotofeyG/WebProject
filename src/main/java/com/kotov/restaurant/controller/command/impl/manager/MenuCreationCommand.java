package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MENU_CREATION_PAGE;

public class MenuCreationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            if (currentPage.equals(MENU_CREATION_PAGE)) {
                String title = request.getParameter(TITLE);
                String type = request.getParameter(TYPE);
                String[] mealIdArray = request.getParameterValues(SELECTED);
                if (service.addNewMenu(title, type, mealIdArray)) {
                    request.setAttribute(MENU_CREATION_RESULT, VALID);
                } else {
                    request.setAttribute(MENU_CREATION_RESULT, INVALID);
                }
            }
            List<Meal> meals = service.findAllMeals();
            if (meals.size() != 0) {
                request.setAttribute(MEAL_SEARCH_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(MEAL_SEARCH_RESULT, Boolean.FALSE);
            }
            request.setAttribute(ALL_MEALS, meals);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(MENU_CREATION_PAGE);
        logger.log(Level.DEBUG, " New menu was created successfully");
        return router;
    }
}