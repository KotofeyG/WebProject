package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class FindAllMealsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        try {
            if (request.getParameter(PAGE).equals(MENU_MANAGEMENT)) {
                router.setPagePath(MENU_MANAGEMENT_PAGE);
            } else if (request.getParameter(PAGE).equalsIgnoreCase(MEAL_MANAGEMENT)) {
                router.setPagePath(MEAL_MANAGEMENT_PAGE);
            } else if (request.getParameter(PAGE).equals(MENU_CREATION)) {
                router.setPagePath(MENU_CREATION_PAGE);
            } else if (request.getParameter(PAGE).equalsIgnoreCase(MENU_UPDATE)) {
                if (request.getParameterValues(SELECTED).length != 1) {
                    router.setPagePath(MENU_MANAGEMENT_PAGE);
                    request.setAttribute(ALL_MENUS, service.findAllMenu());
                } else {
                    router.setPagePath(MENU_UPDATE_PAGE);
                    Menu menu = service.findMenuById(request.getParameter(SELECTED));
                    List<Meal> allMeals = service.findAllMeals();
                    List<Meal> menuMeals = menu.getMeals();
                    Map<Meal, Boolean> result = new LinkedHashMap<>();
                    for (Meal meal : allMeals) {
                        if (menuMeals.contains(meal)) {
                            result.put(meal, Boolean.TRUE);
                        } else {
                            result.put(meal, Boolean.FALSE);
                        }
                    }
                    request.setAttribute(ALL_MEALS, result);
                    request.setAttribute(MENU, menu);
                }
                return router;
            }
            request.setAttribute(ALL_MEALS, service.findAllMeals());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException(e);
        }
        return router;
    }
}