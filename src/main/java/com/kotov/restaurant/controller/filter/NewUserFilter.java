package com.kotov.restaurant.controller.filter;

import com.kotov.restaurant.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.kotov.restaurant.controller.command.ParamName.*;

@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.FORWARD})
public class NewUserFilter implements Filter {                          // in the process of revision
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute(USER);
        if (user == null) {
            user = new User();
            user.setRole(User.Role.GUEST);
            session.setAttribute(USER, user);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}