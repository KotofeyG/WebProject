package com.kotov.restaurant.controller.command.impl.client;

import com.kotov.restaurant.controller.Router;
import com.kotov.restaurant.controller.command.Command;
import com.kotov.restaurant.exception.CommandException;
import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kotov.restaurant.controller.Router.RouteType.REDIRECT;
import static com.kotov.restaurant.controller.command.ParamName.*;
import static com.kotov.restaurant.controller.command.AttributeName.*;
import static com.kotov.restaurant.controller.command.PagePath.*;

public class AddNewAddressCommand implements Command {
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router();
        HttpSession session = request.getSession();
        StringBuilder paramPagePath = new StringBuilder();
        User user = (User) request.getSession().getAttribute(SESSION_USER);
        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(CITY, request.getParameter(CITY));
        dataCheckResult.put(STREET, request.getParameter(STREET));
        dataCheckResult.put(BUILDING, request.getParameter(BUILDING));
        dataCheckResult.put(BUILDING_BLOCK, request.getParameter(BUILDING_BLOCK));
        dataCheckResult.put(FLAT, request.getParameter(FLAT));
        dataCheckResult.put(ENTRANCE, request.getParameter(ENTRANCE));
        dataCheckResult.put(FLOOR, request.getParameter(FLOOR));
        dataCheckResult.put(INTERCOM_CODE, request.getParameter(INTERCOM_CODE));
        try {
            boolean actionResult = userService.insertUserAddress(user.getId(), dataCheckResult);
            if (actionResult) {
                router.setRouterType(REDIRECT);
            } else {
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
            List<Address> addressList = userService.findUserAddresses(user.getId());
            session.setAttribute(ADDRESS_LIST, addressList);
            paramPagePath.append(SETTINGS_PAGE).append(PARAMETERS_START).append(ADDRESS_ADDING_RESULT)
                    .append(EQUALS).append(actionResult);
            router.setPagePath(paramPagePath.toString());
            return router;
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "New Address cannot be added to user:", e);
            throw new CommandException("New Address cannot be added to user:", e);
        }
    }
}