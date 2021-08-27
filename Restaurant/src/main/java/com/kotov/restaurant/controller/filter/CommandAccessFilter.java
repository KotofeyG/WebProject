package com.kotov.restaurant.controller.filter;

import com.kotov.restaurant.controller.command.CommandType;
import com.kotov.restaurant.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.ParamName.COMMAND;
import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;

/**
 * @author Denis Kotov
 *
 * The type Command access filter.
 */
@WebFilter(urlPatterns = {"/*"})
public class CommandAccessFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String commandStr = request.getParameter(COMMAND);
        logger.log(Level.DEBUG, "Access check. Command is " + commandStr);
        try {
            CommandType commandType = CommandType.valueOf(commandStr.toUpperCase());
            EnumSet<User.Role> allowedRoles = commandType.getAllowedRoles();
            Optional<Object> optionalUser = Optional.ofNullable(session.getAttribute(SESSION_USER));
            if (optionalUser.isPresent()) {
                User user = (User) optionalUser.get();
                logger.log(Level.DEBUG, "User role is " + user.getRole());
                if (allowedRoles.contains(user.getRole())) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(403);
                }
            } else {
                if(allowedRoles.contains(User.Role.GUEST)){
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(403);
                }
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.log(Level.DEBUG, "Command is absent");
            filterChain.doFilter(request, response);
        }
    }
}