package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.PagePath;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class AddToCartCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService mealService = ServiceProvider.getInstance().getMealService();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        ServletContext servletContext = request.getServletContext();
        User user = (User) session.getAttribute(USER_ATTR);
        String mealId = request.getParameter(SELECTED);
        String mealNumber = request.getParameter(MEAL_NUMBER);
        String product = request.getParameter(PRODUCT);
        Menu currentMenu = (Menu) servletContext.getAttribute(product);
        System.out.println(product);
        try {
            mealService.insertMealToUserCart(user.getId(), mealId, mealNumber);
            List<Meal> meals;
            if (currentMenu.getTitle().equals(COMMON)) {
                meals = menuService.findMealsByType(product);
            } else {
                meals = menuService.findMealsForMenu(currentMenu.getId());
            }
            meals.removeIf(meal -> !meal.isActive());
            currentMenu.setMeals(meals);
            request.setAttribute(PRODUCT, currentMenu);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(PagePath.PRODUCT_PAGE);
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}