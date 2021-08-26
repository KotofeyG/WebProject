package com.kotov.restaurant.controller.filter;

import com.kotov.restaurant.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;

/**
 * @author Denis Kotov
 *
 * The type New user installation filter.
 */
@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.FORWARD})
public class NewUserInstallationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        Optional<Object> userOptional = Optional.ofNullable(session.getAttribute(SESSION_USER));
        if (userOptional.isEmpty()) {
            session.setAttribute(SESSION_USER, new User(User.Role.GUEST));
        }
        chain.doFilter(request, response);
    }
}