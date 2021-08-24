package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class MenuDeleteCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String menuId = request.getParameter(SELECTED_MENU);
        boolean result;
        try {
            result = menuService.deleteMenuById(menuId);
            if (result) {
                String menuType = request.getParameter(TYPE);
                ServletContext servletContext = request.getServletContext();
                servletContext.removeAttribute(menuType);
            }
            request.setAttribute(MENU_ACTION_RESULT, result);
            List<Menu> menus = menuService.findAllMenu();
            session.setAttribute(MENU_LIST, menus);
            router.setPagePath(MENU_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to delete menu with id " + menuId, e);
            throw new CommandException("Impossible to delete menu with id " + menuId, e);
        }
    }
}