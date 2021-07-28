package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.PagePath;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class AddNewMealCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {                    // change realization
        Router router = new Router();

        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(TITLE, request.getParameter(TITLE));
        dataCheckResult.put(TYPE, request.getParameter(TYPE));
        dataCheckResult.put(PRICE, request.getParameter(PRICE));
        dataCheckResult.put(RECIPE, request.getParameter(RECIPE));
        dataCheckResult.put(IMAGE, request.getParameter(IMAGE));
        logger.log(Level.TRACE, "Meal image param : " + request.getParameter(IMAGE));

        try {                                                                                      //to add valid fields
            if (service.addNewMeal(dataCheckResult)) {
                request.setAttribute(MEAL_CREATION_DATA, VALID);
            } else {
                if (dataCheckResult.get(INVALID_MEAL).equals(INVALID_MEAL_REGISTERED_FIELDS)) {
                    request.setAttribute(MEAL_CREATION_DATA, INVALID);
                } else {
                    request.setAttribute(MEAL_CREATION_DATA, NOT_UNIQUE);
                }
            }
            request.setAttribute(ALL_MEALS, service.findAllMeals());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException(e);
        }

        router.setPagePath(PagePath.MEAL_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, getClass().getSimpleName() + " is completed successfully");
        return router;
    }
}