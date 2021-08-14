package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
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
import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;

public class MealCreationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

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
            if (menuService.addNewMeal(dataCheckResult, imageInputStream)) {
                request.setAttribute(MEAL_CREATION_DATA, VALID);
            } else {
                if (dataCheckResult.get(MEAL_CREATION_DATA).equals(INVALID)) {
                    request.setAttribute(MEAL_CREATION_DATA, INVALID);
                } else {
                    request.setAttribute(MEAL_CREATION_DATA, NOT_UNIQUE);
                }
            }
            List<Meal> meals = menuService.findAllMeals();
            if (meals.size() != 0) {
                request.setAttribute(MEAL_SEARCH_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(MEAL_SEARCH_RESULT, Boolean.FALSE);
            }
            request.setAttribute(MEAL_LIST, meals);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to create meal:", e);
            throw new CommandException("Impossible to create meal:", e);
        } catch (ServletException | IOException e) {
            logger.log(Level.ERROR, "Impossible to get image of meal:", e);
            throw new CommandException("Impossible to get image of meal:", e);
        }
        router.setPagePath(MEAL_MANAGEMENT_PAGE);
        return router;
    }
}