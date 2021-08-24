package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.PaginationItem;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.validator.MealValidator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.model.dao.BaseDao.PAGE_SIZE;
import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class ShowProductInfoCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String selectedProductType = request.getParameter(SELECTED_PRODUCT_TYPE);
        try {
            if (MealValidator.isMealTypeValid(selectedProductType)) {
                ServletContext servletContext = request.getServletContext();
                Menu currentMenu = (Menu) servletContext.getAttribute(selectedProductType);
                int pageToDisplay = getPage(request);
                if (currentMenu == null) {
                    currentMenu = new Menu(EMPTY, Menu.Type.valueOf(selectedProductType.toUpperCase()));
                    servletContext.setAttribute(currentMenu.getType().toString(), currentMenu);
                } else if (!currentMenu.getTitle().equals(EMPTY)) {
                    List<Meal> meals = menuService.findAvailableMealsForMenu(currentMenu.getId(), pageToDisplay);
                    currentMenu.setMeals(meals);
                }
                int mealCount = menuService.getAvailableMealCountForMenu(currentMenu.getId());
                session.setAttribute(PAGE_NUMBER, pageToDisplay);
                session.setAttribute(PAGINATION_ITEM, new PaginationItem(mealCount, pageToDisplay, PAGE_SIZE));
                session.setAttribute(CURRENT_MENU, currentMenu);
                session.setAttribute(CURRENT_PRODUCT_TYPE, selectedProductType);
            } else {
                request.setAttribute(WRONG_PARAMETER, Boolean.TRUE);
            }
            router.setPagePath(PRODUCT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Current available meals cannot be found:", e);
            throw new CommandException("Current available meals cannot be found:", e);
        }
    }
}