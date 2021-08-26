package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class DeleteMealsFromCartCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        StringBuilder paramPagePath = new StringBuilder();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        String[] mealIdArray = request.getParameterValues(MEAL_ID_ARRAY);
        try {
            boolean actionResult = userService.deleteMealsFromCartByUserId(user.getId(), mealIdArray);
            UserCommandsOverallControl.setCartInfoToAttribute(request);
            paramPagePath.append(CART_PAGE).append(PARAMETERS_START).append(ACTION_RESULT).append(EQUALS)
                    .append(actionResult);
            router.setPagePath(paramPagePath.toString());
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Meals cannot be deleted from user cart:", e);
            throw new CommandException("Meals cannot be deleted from user cart:", e);
        }
    }
}