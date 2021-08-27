package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import jakarta.servlet.http.HttpServletRequest;

import static com.kotov.restaurant.controller.command.PagePath.MENU_CREATION_PAGE;

/**
 * Show menu creation info command
 * Used by managers to show {@link Meal} to create new {@link Menu}
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class ShowMenuCreationInfoCommand implements Command {

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        ManagerCommandsOverallControl.setMenuCreationInfo(request);
        router.setPagePath(MENU_CREATION_PAGE);
        return router;
    }
}