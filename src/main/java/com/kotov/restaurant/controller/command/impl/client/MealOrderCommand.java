package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.controller.command.PagePath;
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealOrderCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        User user = (User) request.getSession().getAttribute(USER_ATTR);
        long userId = user.getId();
        String[] mealIdArray = request.getParameterValues(MEAL_ID);
        String[] mealNumberArray = request.getParameterValues(MEAL_NUMBER);
//        String address = request.getParameter(ADDRESS);
//        String time = request.getParameter(DELIVERY_TIME);

        Map<String, String> orderedMeals = new LinkedHashMap<>();
        for (int i = 0; i < mealIdArray.length; i++) {
            orderedMeals.put(mealIdArray[i], mealNumberArray[i]);
        }
        System.out.println(Arrays.toString(mealIdArray));
        System.out.println(Arrays.toString(mealNumberArray));
//        System.out.println(address);
//        System.out.println(time);
        try {
            if (!orderService.addOrder(userId, orderedMeals)) {
                //to do
            }
            Map<Meal, Integer> cart = userService.findMealsInCartByUserId(userId);
            List<Address> addresses = userService.findUserAddresses(userId);
            request.setAttribute(CART, cart);
            request.setAttribute(ADDRESSES, addresses);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Command cannot be completed:", e);
            throw new CommandException("Command cannot be completed:", e);
        }
        logger.log(Level.DEBUG, "Method execute is completed successfully");
        router.setPagePath(PagePath.CART_PAGE);
        return router;
    }
}