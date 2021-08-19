package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.PaginationItem;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.validator.MealValidator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.model.dao.BaseDao.PAGE_SIZE;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.PRODUCT_PAGE;

public class AddMealToCartCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SESSION_USER);
        String mealId = request.getParameter(SELECTED);
        String mealNumber = request.getParameter(MEAL_NUMBER);
        String selectedProductType = request.getParameter(SELECTED_PRODUCT_TYPE);
        try {
            request.setAttribute(RESULT_OF_INSERT, menuService.addMealToUserCart(user.getId(), mealId, mealNumber));
            if (MealValidator.isMealTypeExist(selectedProductType)) {
                ServletContext servletContext = request.getServletContext();
                Menu currentMenu = (Menu) servletContext.getAttribute(selectedProductType);
                int pageToDisplay = Integer.parseInt(request.getParameter(PAGE_NUMBER));
                if (!currentMenu.getTitle().equals(EMPTY)) {
                    List<Meal> meals = menuService.findMealsForMenuByPresence(currentMenu.getId(), pageToDisplay);
                    currentMenu.setMeals(meals);
                }
                int mealCount = menuService.getMealCountForMenu(currentMenu.getId());
                request.setAttribute(PAGE_NUMBER, pageToDisplay);
                request.setAttribute(PAGINATION_ITEM, new PaginationItem(mealCount, pageToDisplay, PAGE_SIZE));
                request.setAttribute(CURRENT_MENU, currentMenu);
                request.setAttribute(CURRENT_PRODUCT_TYPE, selectedProductType);
            } else {
                request.setAttribute(WRONG_PARAMETER, Boolean.TRUE);
                logger.log(Level.WARN, "Unexpected value. Wrong request parameter: product type - " + selectedProductType);
            }
            router.setPagePath(PRODUCT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Meal cannot be added to user cart:", e);
            throw new CommandException("Meal cannot be added to user cart:", e);
        }
    }
}