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
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String menuId = request.getParameter(SELECTED_MENU);
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        try {
            if (currentPage.equals(MENU_UPDATE_PAGE)) {
                String action = request.getParameter(ACTION);
                String mealId = request.getParameter(SELECTED);
                if (action.equals(APPEND)) {
                    service.addMealToMenu(menuId, mealId);
                } else if (action.equals(REMOVE)) {
                    service.deleteMealFromMenu(menuId, mealId);
                }
            }
            Optional<Menu> menuOptional = service.findMenuById(menuId);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
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
                request.setAttribute(SELECTED_MENU, menu);
                router.setPagePath(MENU_UPDATE_PAGE);
            } else {
                List<Menu> menus = service.findAllMenu();
                if (menus.size() != 0) {
                    request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
                } else {
                    request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
                }
                request.setAttribute(ALL_MENUS, menus);
                request.setAttribute(UNSELECTED_MENU, Boolean.TRUE);
                router.setPagePath(MENU_MANAGEMENT_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        return router;
    }
}