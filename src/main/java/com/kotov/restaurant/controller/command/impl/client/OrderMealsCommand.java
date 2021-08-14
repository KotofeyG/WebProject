package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.kotov.restaurant.controller.command.PagePath.CART_PAGE;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;

public class OrderMealsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        long userId = user.getId();
        String[] mealIdArray = request.getParameterValues(MEAL_ID);
        String[] mealNumberArray = request.getParameterValues(MEAL_NUMBER);
        Map<String, String> orderedMeals = new LinkedHashMap<>();
        for (int i = 0; i < mealIdArray.length; i++) {
            orderedMeals.put(mealIdArray[i], mealNumberArray[i]);
        }
        String time = request.getParameter(DELIVERY_TIME);
        String paymentType = request.getParameter(PAYMENT_TYPE);
        String addressIdStr = request.getParameter(ADDRESS);
        try {
            if (addressIdStr == null) {
                Map<String, String> dataCheckResult = new HashMap<>();
                dataCheckResult.put(CITY, request.getParameter(CITY));
                dataCheckResult.put(STREET, request.getParameter(STREET));
                dataCheckResult.put(BUILDING, request.getParameter(BUILDING));
                dataCheckResult.put(BUILDING_BLOCK, request.getParameter(BUILDING_BLOCK));
                dataCheckResult.put(FLAT, request.getParameter(FLAT));
                dataCheckResult.put(ENTRANCE, request.getParameter(ENTRANCE));
                dataCheckResult.put(FLOOR, request.getParameter(FLOOR));
                dataCheckResult.put(INTERCOM_CODE, request.getParameter(INTERCOM_CODE));
                // to do
            } else {
                if (orderService.addOrder(orderedMeals, userId, addressIdStr, time, paymentType)) {
                    //to do
                }
            }
            Map<Meal, Integer> cart = userService.findMealsInCartByUserId(userId);
            List<Address> addresses = userService.findUserAddresses(userId);
            request.setAttribute(CART, cart);
            request.setAttribute(ADDRESS_LIST, addresses);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException("Command cannot be completed:", e);
        }
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        router.setPagePath(CART_PAGE);
        return router;
    }
}