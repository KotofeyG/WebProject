package com.kotov.restaurant.controller.command.impl.user;

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

import static com.kotov.restaurant.controller.command.PagePath.*;

public class RollCommand implements Command {                       // in the process of revision
    private static final Logger logger = LogManager.getLogger();
    private static final MenuService service = ServiceProvider.getInstance().getMenuService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ServletContext servletContext = request.getServletContext();
        try {
            if (servletContext.getAttribute("ROLLS") == null) {
                Menu menu = new Menu("Common", "ROLLS", service.findAllMeals());
                servletContext.setAttribute("ROLLS", menu);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Method execute cannot be completed:", e);
            throw new CommandException("Method execute cannot be completed:", e);
        }
        router.setPagePath(ROLL_PAGE);
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        return router;
    }
}