//package com.kotov.restaurant.controller.filter;
//
//import com.kotov.restaurant.controller.command.AttributeName;
//import com.kotov.restaurant.controller.command.CommandProvider;
//import com.kotov.restaurant.model.entity.User;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.io.IOException;
//import java.util.EnumSet;
//
//import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;
//
//@WebFilter("/controller")
//public class SecurityFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        User user = (User) request.getSession().getAttribute(SESSION_USER);
//        User.Role userRole;
//        userRole = user != null ? user.getRole() : User.Role.GUEST;
//        EnumSet<User.Role> userRoles = CommandProvider.getInstance().getCommandAccessLevel(request);
//        if (!userRoles.contains(userRole)) {
//            request.setAttribute(ERRORS_ON_ERROR_PAGE, "You should login or register");
//            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        } else {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//    }
//}