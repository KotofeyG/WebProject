package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.AttributeName.*;

class UserCommandsOverallControl {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    static void setCartInfoToAttribute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SESSION_USER);
        try {
            Map<Meal, Integer> cart = userService.findMealsInCartByUserId(user.getId());
            List<Address> addresses = userService.findUserAddresses(user.getId());
            request.setAttribute(CART, cart);
            request.setAttribute(ADDRESS_LIST, addresses);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find cart info for user with id " + user.getId(), e);
            throw new CommandException("Impossible to find cart info for user with id " + user.getId(), e);
        }
    }
}