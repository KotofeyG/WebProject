package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.List;

import static com.kotov.restaurant.controller.command.AttributeName.MENU_LIST;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;

public class MenuManagementCommand implements Command {
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
        try {
            List<Menu> menus = menuService.findAllMenu();
            session.setAttribute(MENU_LIST, menus);
            router.setPagePath(MENU_MANAGEMENT_PAGE);
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find menus:", e);
            throw new CommandException("Impossible to find menus:", e);
        }
    }
}