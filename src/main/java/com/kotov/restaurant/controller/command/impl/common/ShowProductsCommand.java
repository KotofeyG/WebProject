package com.kotov.restaurant.controller.command.impl.common;

import com.kotov.restaurant.controller.command.Page;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.PRODUCT_PAGE;
import static com.kotov.restaurant.model.dao.BaseDao.PAGE_SIZE;

public class ShowProductsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String selectedProductType = request.getParameter(SELECTED_PRODUCT_TYPE);

        try {
            if (MealValidator.isMealTypeExist(selectedProductType)) {
                ServletContext servletContext = request.getServletContext();
                Menu currentMenu = (Menu) servletContext.getAttribute(selectedProductType);
                int pageToDisplay = getPage(request);

                if (currentMenu == null) {
                    currentMenu = new Menu(EMPTY, Menu.Type.valueOf(selectedProductType.toUpperCase()));
                    servletContext.setAttribute(currentMenu.getType().toString(), currentMenu);
                } else if (!currentMenu.getTitle().equals(EMPTY)) {
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