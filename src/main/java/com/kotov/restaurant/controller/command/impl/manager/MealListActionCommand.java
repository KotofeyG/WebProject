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

import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealListActionCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();

        String action = request.getParameter(ACTION);
        String[] mealIdArray = request.getParameterValues(SELECTED);

        try {
            if (action.equals(ACTION_BLOCK)) {
                service.updateMealStatuses(NOT_ACTIVE, mealIdArray);
            } else if (action.equals(ACTION_UNBLOCK)) {
                service.updateMealStatuses(ACTIVE, mealIdArray);
            } else if (action.equals(ACTION_DELETE)) {
                service.removeMeals(mealIdArray);
            }
            request.setAttribute(ALL_MEALS, service.findAllMeals());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException("Command cannot be completed:", e);
        }

        router.setPagePath(MEAL_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, getClass().getSimpleName() + " method execute is completed successfully");
        return router;
    }
}