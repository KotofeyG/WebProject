package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.CART_PAGE;

public class DeleteFromCartCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        long userId = user.getId();
        String[] mealIdArray = request.getParameterValues(MEAL_ID);
        try {
            userService.deleteMealsFromCartByUserId(userId, mealIdArray);
            Map<Meal, Integer> cart = userService.findMealsInCartByUserId(userId);
            List<Address> addresses = userService.findUserAddresses(userId);
            request.setAttribute(CART, cart);
            request.setAttribute(ADDRESS_LIST, addresses);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to delete meals from cart:", e);
            throw new CommandException("Impossible to delete meals from cart:", e);
        }
        router.setPagePath(CART_PAGE);
        return router;
    }
}