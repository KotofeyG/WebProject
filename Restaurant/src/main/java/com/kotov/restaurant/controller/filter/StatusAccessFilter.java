package com.kotov.restaurant.controller.filter;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.User;
import com.kotov.restaurant.model.service.ServiceProvider;
import com.kotov.restaurant.model.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;
import static com.kotov.restaurant.controller.command.AttributeName.ACCOUNT_BLOCKAGE_MESSAGE;
import static jakarta.servlet.http.HttpServletResponse.*;

/**
 * @author Denis Kotov
 *
 * The type Status access filter.
 */
@WebFilter(urlPatterns = {"/*"})
public class StatusAccessFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SESSION_USER);
        try {
            User.Status currentStatus = userService.findUserStatusById(user.getId());
            if (user.getRole() != User.Role.ADMIN && currentStatus == User.Status.BLOCKED) {
                session.setAttribute(SESSION_USER, new User(User.Role.GUEST));
                logger.log(Level.INFO, "An attempt to access a blocked account");
                response.sendError(SC_FORBIDDEN, ACCOUNT_BLOCKAGE_MESSAGE);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Impossible to check the user status:", e);
            response.sendError(SC_INTERNAL_SERVER_ERROR);
        }
    }
}