package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.service.MealService;
import com.kotov.restaurant.model.service.ServiceProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.kotov.restaurant.controller.command.PagePath.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class FindAllMenuCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final MealService service = ServiceProvider.getInstance().getMealService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        List<Menu> menus;
        try {
            menus = service.findAllMenu();
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException(e);
        }
        request.setAttribute(ALL_MENUS, menus);
        router.setPagePath(MENU_MANAGEMENT_PAGE);
        logger.log(Level.DEBUG, "Menus were added. Result is " + menus);
        return router;
    }
}