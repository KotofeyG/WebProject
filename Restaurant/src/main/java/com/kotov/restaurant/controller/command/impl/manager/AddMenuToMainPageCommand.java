package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.SELECTED_MENU;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;

/**
 *
 * Add menu to main page command
 * {@link User} manager can add {@link Menu} to the main page.
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class AddMenuToMainPageCommand implements Command {
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        ServletContext servletContext = request.getServletContext();
        String menuId = request.getParameter(SELECTED_MENU);
        try {
            Optional<Menu> menuOptional = menuService.findMenuById(menuId);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                servletContext.setAttribute(menu.getType().toString(), menu);
                request.setAttribute(MENU_ACTION_RESULT, Boolean.TRUE);
                logger.log(Level.INFO, "Menu was added to main page. Result is " + menu);
            } else {
                request.setAttribute(MENU_ACTION_RESULT, Boolean.FALSE);
            }
            List<Menu> menus = menuService.findAllMenu();
            session.setAttribute(MENU_LIST, menus);
            router.setPagePath(MENU_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to add menu to main page:", e);
            throw new CommandException("Impossible to add menu to main page:", e);
        }
    }
}