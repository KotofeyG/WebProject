package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;

public class MealListActionCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        try {
            String action = request.getParameter(ACTION);
            String[] mealIdArray = request.getParameterValues(SELECTED);
            action = action != null ? action : EMPTY;
            Boolean actionResult = switch (action) {
                case BLOCK -> menuService.updateMealStatusesById(NOT_ACTIVE, mealIdArray);
                case UNBLOCK -> menuService.updateMealStatusesById(ACTIVE, mealIdArray);
                case DELETE -> menuService.deleteMealsById(mealIdArray);
                default -> {
                    logger.log(Level.WARN, "Unexpected value: " + action);
                    yield false;
                }
            };
            request.setAttribute(MEAL_ACTION_RESULT, actionResult);
            List<Meal> meals = menuService.findAllMeals();
            session.setAttribute(MEAL_LIST, meals);
            router.setPagePath(MEAL_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to do action with meals:", e);
            throw new CommandException("Impossible to do action with meals:", e);
        }
    }
}