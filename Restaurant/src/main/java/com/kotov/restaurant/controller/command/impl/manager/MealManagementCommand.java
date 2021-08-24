package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.MEAL_LIST;
import static com.kotov.restaurant.controller.command.PagePath.MEAL_MANAGEMENT_PAGE;

public class MealManagementCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        try {
            List<Meal> meals = menuService.findAllMeals();
            session.setAttribute(MEAL_LIST, meals);
            router.setPagePath(MEAL_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find meals:", e);
            throw new CommandException("Impossible to find meals:", e);
        }
    }
}