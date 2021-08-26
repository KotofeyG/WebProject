package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.util.validator.MealValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.*;

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class OrderMealsCommand implements Command {
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    /**
     * @param request the HttpServletRequest
     * @return the {@link Router}
     * @throws CommandException if the request could not be handled.
     */
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        StringBuilder paramPagePath = new StringBuilder();
        String[] mealIdArray = request.getParameterValues(MEAL_ID_ARRAY);
        String[] mealNumberArray = request.getParameterValues(MEAL_NUMBER);
        if (!MealValidator.isMealInOrderExist(mealIdArray, mealNumberArray)) {
            paramPagePath.append(CART_PAGE).append(PARAMETERS_START).append(ACTION_RESULT).append(EQUALS)
                    .append(Boolean.FALSE);
            UserCommandsOverallControl.setCartInfoToAttribute(request);
            router.setPagePath(paramPagePath.toString());
            return router;
        }
        Map<String, String> orderedMeals = new HashMap<>();
        for (int i = 0; i < mealIdArray.length; i++) {
            orderedMeals.put(mealIdArray[i], mealNumberArray[i]);
        }
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        String time = request.getParameter(DELIVERY_TIME);
        String paymentType = request.getParameter(PAYMENT_TYPE);
        String addressIdStr = request.getParameter(ADDRESS);
        boolean actionResult;
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
                actionResult = orderService.insertOrder(orderedMeals, user.getId(), dataCheckResult, time, paymentType);
                if (!actionResult) {
                    for (String key : dataCheckResult.keySet()) {
                        String validationResult = dataCheckResult.get(key);
                        if (Boolean.parseBoolean(validationResult)) {
                            switch (key) {
                                case CITY -> request.setAttribute(VALID_CITY, request.getParameter(CITY));
                                case STREET -> request.setAttribute(VALID_STREET, request.getParameter(STREET));
                                case BUILDING -> request.setAttribute(VALID_BUILDING, request.getParameter(BUILDING));
                                case BUILDING_BLOCK -> request.setAttribute(VALID_BUILDING_BLOCK, request.getParameter(BUILDING_BLOCK));
                                case FLAT -> request.setAttribute(VALID_FLAT, request.getParameter(FLAT));
                                case ENTRANCE -> request.setAttribute(VALID_ENTRANCE, request.getParameter(ENTRANCE));
                                case FLOOR -> request.setAttribute(VALID_FLOOR, request.getParameter(FLOOR));
                                case INTERCOM_CODE -> request.setAttribute(VALID_INTERCOM_CODE, request.getParameter(INTERCOM_CODE));
                            }
                        } else {
                            switch (validationResult) {
                                case INVALID_CITY -> request.setAttribute(INVALID_CITY, Boolean.TRUE);
                                case INVALID_STREET -> request.setAttribute(INVALID_STREET, Boolean.TRUE);
                                case INVALID_BUILDING -> request.setAttribute(INVALID_BUILDING, Boolean.TRUE);
                                case INVALID_BUILDING_BLOCK -> request.setAttribute(INVALID_BUILDING_BLOCK, Boolean.TRUE);
                                case INVALID_FLAT -> request.setAttribute(INVALID_FLAT, Boolean.TRUE);
                                case INVALID_ENTRANCE -> request.setAttribute(INVALID_ENTRANCE, Boolean.TRUE);
                                case INVALID_FLOOR -> request.setAttribute(INVALID_FLOOR, Boolean.TRUE);
                                case INVALID_INTERCOM_CODE -> request.setAttribute(INVALID_INTERCOM_CODE, Boolean.TRUE);
                            }
                        }
                    }
                }
            } else {
                actionResult = orderService.insertOrder(orderedMeals, user.getId(), addressIdStr, time, paymentType);
            }
            UserCommandsOverallControl.setCartInfoToAttribute(request);
            paramPagePath.append(CART_PAGE).append(PARAMETERS_START).append(ACTION_RESULT).append(EQUALS)
                    .append(actionResult);
            router.setPagePath(paramPagePath.toString());
            if (actionResult) {
                router.setRouterType(REDIRECT);
            }
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Order cannot be executed:", e);
            throw new CommandException("Order cannot be executed", e);
        }
    }
}