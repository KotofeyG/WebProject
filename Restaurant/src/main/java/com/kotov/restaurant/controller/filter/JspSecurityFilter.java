package com.kotov.restaurant.controller.filter;

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
import java.util.Optional;
import java.util.Set;

import static com.kotov.restaurant.controller.command.AttributeName.SESSION_USER;
import static com.kotov.restaurant.controller.command.PagePath.*;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * @author Denis Kotov
 *
 * The type Jsp security filter.
 */
@WebFilter(urlPatterns = {"*.jsp"})
public class JspSecurityFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private static final String START_URI = "/index.jsp";
    private Set<String> guestPages;
    private Set<String> clientPages;
    private Set<String> managerPages;
    private Set<String> adminPages;

    @Override
    public void init(FilterConfig filterConfig) {
        guestPages = Set.of(INDEX_PAGE
                , MAIN_PAGE
                , PRODUCT_PAGE
                , REGISTRATION_PAGE
                , ACCOUNT_CREATION_DETAILS_PAGE
                , ERROR_400_PAGE);
        clientPages = Set.of(INDEX_PAGE
                , MAIN_PAGE
                , PRODUCT_PAGE
                , ORDER_PAGE
                , CART_PAGE
                , SETTINGS_PAGE
                , ERROR_400_PAGE);
        managerPages = Set.of(INDEX_PAGE
                , MAIN_PAGE
                , PRODUCT_PAGE
                , MEAL_MANAGEMENT_PAGE
                , MENU_MANAGEMENT_PAGE
                , MENU_CREATION_PAGE
                , MENU_UPDATE_PAGE
                , ORDER_CONFIRMATION_PAGE
                , ERROR_400_PAGE);
        adminPages = Set.of(INDEX_PAGE
                , MAIN_PAGE
                , PRODUCT_PAGE
                , USER_MANAGEMENT_PAGE
                , ERROR_400_PAGE);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getServletPath();
        System.out.println(requestURI);
        HttpSession session = request.getSession();
        boolean isGuestPage = guestPages.stream().anyMatch(requestURI::contains);
        boolean isClientPage = clientPages.stream().anyMatch(requestURI::contains);
        boolean isManagerPage = managerPages.stream().anyMatch(requestURI::contains);
        boolean isAdminPage = adminPages.stream().anyMatch(requestURI::contains);
        Object userAttribute = session.getAttribute(SESSION_USER);
        Optional<Object> userOptional = Optional.ofNullable(userAttribute);
        if (userOptional.isPresent()) {
            User user = (User) userOptional.get();
            User.Role role = user.getRole();
            if (role == User.Role.GUEST && isGuestPage) {
                chain.doFilter(request, response);
            } else if (role == User.Role.CLIENT && isClientPage) {
                chain.doFilter(request, response);
            } else if (role == User.Role.MANAGER && isManagerPage) {
                chain.doFilter(request, response);
            } else if (role == User.Role.ADMIN && isAdminPage) {
                chain.doFilter(request, response);
            } else {
                response.sendError(SC_FORBIDDEN);
            }
        } else {
            if (isGuestPage || requestURI.equals(START_URI)) {
                chain.doFilter(request, response);
            } else {
                logger.log(Level.WARN, "Page doesn't exist.");
                response.sendError(SC_FORBIDDEN);
            }
        }
    }
}