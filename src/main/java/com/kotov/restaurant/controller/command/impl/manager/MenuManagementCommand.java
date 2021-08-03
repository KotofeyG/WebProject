package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
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
import static com.kotov.restaurant.controller.command.AttributeName.MENU_SEARCH_RESULT;
import static com.kotov.restaurant.controller.command.PagePath.MENU_MANAGEMENT_PAGE;

public class MenuManagementCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        try {
            List<Menu> menus = service.findAllMenu();
            if (menus.size() != 0) {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.TRUE);
            } else {
                request.setAttribute(MENU_SEARCH_RESULT, Boolean.FALSE);
            }
            request.setAttribute(ALL_MENUS, menus);
            logger.log(Level.DEBUG, "Menus were added. Result is " + menus);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(MENU_MANAGEMENT_PAGE);
        return router;
    }
}