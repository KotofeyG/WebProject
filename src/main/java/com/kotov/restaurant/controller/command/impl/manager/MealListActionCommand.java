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
import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;

public class MealListActionCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            if (currentPage.equals(MEAL_MANAGEMENT_PAGE)) {
                String action = request.getParameter(ACTION);
                String[] mealIdArray = request.getParameterValues(SELECTED);
                Boolean result = switch (action) {
                    case BLOCK -> service.updateMealStatusesById(NOT_ACTIVE, mealIdArray);
                    case UNBLOCK -> service.updateMealStatusesById(ACTIVE, mealIdArray);
                    case DELETE -> service.removeMealsById(mealIdArray);
                    default -> Boolean.FALSE;
                };
                request.setAttribute(MEAL_ACTION_RESULT, result);
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
        router.setPagePath(MEAL_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}