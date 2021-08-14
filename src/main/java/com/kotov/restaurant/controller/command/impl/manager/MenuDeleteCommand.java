package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;

public class MenuDeleteCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        String menuId = request.getParameter(SELECTED_MENU);
        try {
            if (menuService.deleteMenuById(menuId)) {
                request.setAttribute(UNSELECTED_MENU, Boolean.FALSE);           // change attribute name
            } else {
                request.setAttribute(UNSELECTED_MENU, Boolean.TRUE);
            }
            List<Menu> menus = menuService.findAllMenu();                           // pagination?
            if (menus.size() != 0) {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
            }
            request.setAttribute(MENU_LIST, menus);
            router.setPagePath(MENU_MANAGEMENT_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to delete menu with id " + menuId, e);
            throw new CommandException("Impossible to delete menu with id " + menuId, e);
        }
        return router;
    }
}