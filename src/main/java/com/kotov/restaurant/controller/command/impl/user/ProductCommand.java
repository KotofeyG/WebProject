package com.kotov.restaurant.controller.command.impl.user;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class ProductCommand implements Command {                       // in the process of revision
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String product = request.getParameter(PRODUCT);
        ServletContext servletContext = request.getServletContext();
        Menu currentMenu = (Menu) servletContext.getAttribute(product);
        try {
            List<Meal> meals;
            if (currentMenu == null) {
                currentMenu = new Menu(COMMON, Menu.Type.valueOf(product.toUpperCase()));
                servletContext.setAttribute(currentMenu.getType().toString(), currentMenu);
                meals = service.findMealsByType(product);
            } else if (currentMenu.getTitle().equals(COMMON)) {
                meals = service.findMealsByType(product);
            } else {
                meals = service.findMealsForMenu(currentMenu.getId());
            }
            meals.removeIf(meal -> !meal.isActive());
            currentMenu.setMeals(meals);
            request.setAttribute(PRODUCT, currentMenu);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(PRODUCT_PAGE);
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}