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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.PagePath.MENU_UPDATE_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class MenuUpdateCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String action = request.getParameter(ACTION);
        String mealId = request.getParameter(SELECTED);
        String menuId = request.getParameter(MENU);
        try {
            if (action.equals(APPEND)) {
                service.addMealToMenu(menuId, mealId);
            } else if (action.equals(REMOVE)) {
                service.deleteMealFromMenu(menuId, mealId);
            }
            Menu menu = service.findMenuById(request.getParameter(MENU));
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
            router.setPagePath(MENU_UPDATE_PAGE);
        } catch (ServiceException e) {
           throw new CommandException(e);
        }
        return router;
    }
}