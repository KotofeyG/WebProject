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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class AddMenuToMainPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ServletContext servletContext = request.getServletContext();
        String menuId = request.getParameter(SELECTED_MENU);
        try {
            Optional<Menu> menuOptional = menuService.findMenuById(menuId);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                servletContext.setAttribute(menu.getType().toString(), menu);
                request.setAttribute(UNSELECTED_MENU, Boolean.FALSE);
            } else {
                request.setAttribute(UNSELECTED_MENU, Boolean.TRUE);
            }
            List<Menu> menus = menuService.findAllMenu();                               // pagination?
            if (menus.size() != 0) {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
            }
            request.setAttribute(MENU_LIST, menus);
            logger.log(Level.INFO, "Menu were added to main page. Result is " + menus);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to add menu to main page:", e);
            throw new CommandException("Impossible to add menu to main page:", e);
        }
        router.setPagePath(MENU_MANAGEMENT_PAGE);
        return router;
    }
}