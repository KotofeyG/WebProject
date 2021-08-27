package com.kotov.restaurant.controller.command.impl.manager;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;
import com.kotov.restaurant.model.entity.Order;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.MenuService;
import com.kotov.restaurant.model.service.OrderService;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.kotov.restaurant.model.entity.Order.Status.*;
import static com.kotov.restaurant.controller.command.ParamName.SELECTED_PRODUCT_TYPE;
import static com.kotov.restaurant.controller.command.ParamName.SELECTED_MENU;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

/**
 * Utility class for other managers command classes
 *
 * @see Command
 * @see com.kotov.restaurant.controller.command.Command
 */
class ManagerCommandsOverallControl {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();
    private static final MenuService menuService = ServiceProvider.getInstance().getMenuService();
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    static void setActiveOrdersToAttribute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        try {
            Map<Order, User> activeOrders = new LinkedHashMap<>();
            List<Order> activeOrderList = orderService.findOrdersByStatuses(EnumSet.of(IN_PROCESS, APPROVED));
            for (Order order : activeOrderList) {
                Optional<User> optionalUser = userService.findUserById(order.getUserId());
                if (optionalUser.isPresent()) {
                    User client = optionalUser.get();
                    activeOrders.put(order, client);
                } else {
                    logger.log(Level.WARN, "User with id " + order.getUserId() + " doesn't exist");
                }
            }
            session.setAttribute(ORDER_LIST, activeOrders);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to set active orders request attribute:", e);
            throw new CommandException("Impossible to set active orders request attribute:", e);
        }
    }

    static Router setModifiedMenuToAttribute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        String menuId = request.getParameter(SELECTED_MENU);
        try {
            Optional<Menu> menuOptional = menuService.findMenuById(menuId);
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                List<Meal> allMeals = menuService.findMealsByType(menu.getType().toString());
                List<Meal> menuMeals = menu.getMeals();
                Map<Meal, Boolean> markedMeals = new LinkedHashMap<>();
                for (Meal meal : allMeals) {
                    markedMeals.put(meal, menuMeals.contains(meal));
                }
                session.setAttribute(SELECTED_MENU, menu);
                session.setAttribute(MARKED_MEALS, markedMeals);
                router.setPagePath(MENU_UPDATE_PAGE);
            } else {
                List<Menu> menus = menuService.findAllMenu();
                session.setAttribute(MENU_LIST, menus);
                request.setAttribute(MENU_ACTION_RESULT, Boolean.FALSE);
                router.setPagePath(MENU_MANAGEMENT_PAGE);
                logger.log(Level.WARN, "Unexpected action. Wrong parameter: non-existent menuId - " + menuId);
            }
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to set modified menu to request attribute with id " + menuId, e);
            throw new CommandException("Impossible to set modified menu to request attribute with id " + menuId, e);
        }
    }

    static void setMenuCreationInfo(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        String product = request.getParameter(SELECTED_PRODUCT_TYPE);
        try {
            List<Meal> meals = menuService.findMealsByType(Objects.requireNonNullElseGet(product, Meal.Type.ROLL::toString));
            session.setAttribute(MEAL_LIST, meals);
            session.setAttribute(CURRENT_PRODUCT_TYPE, product != null ? product : Meal.Type.ROLL.toString());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to find info for creation of menu:", e);
            throw new CommandException("Impossible to find info for creation of menu:", e);
        }
    }
}