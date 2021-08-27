package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Show menu details command
 * Used by managers to show {@link Meal} to update current {@link Menu}
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
public class ShowMenuDetailsCommand implements Command {

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = ManagerCommandsOverallControl.setModifiedMenuToAttribute(request);
        return router;
    }
}