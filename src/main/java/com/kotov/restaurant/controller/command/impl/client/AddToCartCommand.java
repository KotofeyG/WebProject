package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.Page;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.validator.MealValidator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.PRODUCT_PAGE;
import static com.kotov.restaurant.model.dao.BaseDao.PAGE_SIZE;

public class AddToCartCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService mealService = ServiceProvider.getInstance().getMealService();
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
            mealService.insertMealToUserCart(user.getId(), mealId, mealNumber);
            if (MealValidator.isMealTypeExist(selectedProductType)) {
                ServletContext servletContext = request.getServletContext();
                Menu currentMenu = (Menu) servletContext.getAttribute(selectedProductType);
                int pageToDisplay = getPage(request);

                if (!currentMenu.getTitle().equals(EMPTY)) {
                    List<Meal> meals = menuService.findMealsForMenuByPresence(currentMenu.getId(), pageToDisplay);
                    currentMenu.setMeals(meals);
                }
                if (currentMenu.size() != 0) {
                    int mealCount = menuService.getMealCountForMenu(currentMenu.getId());
                    request.setAttribute(PAGEABLE, new Page(mealCount, pageToDisplay, PAGE_SIZE));
                    request.setAttribute(CURRENT_PRODUCT_TYPE, selectedProductType);
                    request.setAttribute(CURRENT_MENU, currentMenu);
                } else {
                    request.setAttribute(ZERO_NUMBER_OF_MEALS, Boolean.TRUE);
                }
            } else {
                request.setAttribute(WRONG_PARAMETER, Boolean.TRUE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(PRODUCT_PAGE);
        return router;
    }
}