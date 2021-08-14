package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class MenuUpdateCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String menuId = request.getParameter(SELECTED_MENU);
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            if (currentPage.equals(MENU_UPDATE_PAGE)) {                                 // is it right?
                String action = request.getParameter(ACTION);
                String mealId = request.getParameter(SELECTED);

                boolean result;
                if (APPEND.equals(action)) {
                    result = menuService.addMealToMenu(menuId, mealId);
                } else if (REMOVE.equals(action)) {
                    result = menuService.deleteMealFromMenu(menuId, mealId);
                } else {
                    result = false;
                }
                if (!result) {
                    request.setAttribute(ACTION_RESULT, Boolean.FALSE);
                }
            }
            Optional<Menu> menuOptional = menuService.findMenuById(menuId);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                List<Meal> allMeals = menuService.findMealsByType(menu.getType().toString()); // pagination?  overriding?
                List<Meal> menuMeals = menu.getMeals();
                Map<Meal, Boolean> markedMeals = new LinkedHashMap<>();
                for (Meal meal : allMeals) {
                    if (menuMeals.contains(meal)) {
                        markedMeals.put(meal, Boolean.TRUE);
                    } else {
                        markedMeals.put(meal, Boolean.FALSE);
                    }
                }
                request.setAttribute(MARKED_MEALS, markedMeals);
                request.setAttribute(SELECTED_MENU, menu);
                router.setPagePath(MENU_UPDATE_PAGE);
            } else {
                List<Menu> menus = menuService.findAllMenu();                           // pagination?
                if (menus.size() != 0) {
                    request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
                } else {
                    request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
                }
                request.setAttribute(MENU_LIST, menus);
                request.setAttribute(UNSELECTED_MENU, Boolean.TRUE);
                router.setPagePath(MENU_MANAGEMENT_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to update menu with id " + menuId, e);
            throw new CommandException("Impossible to update menu with id " + menuId, e);
        }
        return router;
    }
}