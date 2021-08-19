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

import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.SELECTED_MENU;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;

public class AddMenuToMainPageCommand implements Command {
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
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
                logger.log(Level.INFO, "Menu was added to main page. Result is " + menu);
            } else {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
            }
            List<Menu> menus = menuService.findAllMenu();
            request.setAttribute(MENU_LIST, menus);
            router.setPagePath(MENU_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to add menu to main page:", e);
            throw new CommandException("Impossible to add menu to main page:", e);
        }
    }
}