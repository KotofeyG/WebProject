package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.PagePath;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealCreationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(TITLE, request.getParameter(TITLE));
        dataCheckResult.put(TYPE, request.getParameter(TYPE));
        dataCheckResult.put(PRICE, request.getParameter(PRICE));
        dataCheckResult.put(RECIPE, request.getParameter(RECIPE));
        try {
            Part imagePart = request.getPart(IMAGE);
            InputStream imageInputStream = imagePart.getInputStream();
            if (service.addNewMeal(dataCheckResult, imageInputStream)) {
                request.setAttribute(MEAL_CREATION_DATA, VALID);
            } else {
                if (dataCheckResult.get(MEAL_CREATION_DATA).equals(INVALID)) {
                    request.setAttribute(MEAL_CREATION_DATA, INVALID);
                } else {
                    request.setAttribute(MEAL_CREATION_DATA, NOT_UNIQUE);
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
        } catch (ServletException | IOException e) {
            logger.log(Level.ERROR, "Impossible to get image of meal:", e);
            throw new CommandException("Impossible to get image of meal:", e);
        }
        router.setPagePath(PagePath.MEAL_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}