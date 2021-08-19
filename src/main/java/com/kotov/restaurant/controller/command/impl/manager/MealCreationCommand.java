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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;

public class MealCreationCommand implements Command {
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
            if (menuService.insertNewMeal(dataCheckResult, imageInputStream)) {
                request.setAttribute(MEAL_CREATION_RESULT, VALID);
            } else {
                switch (dataCheckResult.get(MEAL_CREATION_RESULT)) {
                    case INVALID -> request.setAttribute(MEAL_CREATION_RESULT, INVALID);
                    case NOT_UNIQUE -> request.setAttribute(MEAL_CREATION_RESULT, NOT_UNIQUE);
                }
            }
            List<Meal> meals = menuService.findAllMeals();
            request.setAttribute(MEAL_LIST, meals);
            router.setPagePath(MEAL_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to create meal:", e);
            throw new CommandException("Impossible to create meal:", e);
        } catch (ServletException | IOException e) {
            logger.log(Level.ERROR, "Impossible to get image of meal:", e);
            throw new CommandException("Impossible to get image of meal:", e);
        }
    }
}