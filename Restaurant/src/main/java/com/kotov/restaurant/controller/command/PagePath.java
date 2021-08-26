package com.kotov.restaurant.controller.command;

/**
 * @author Denis Kotov
 *
 * The page paths.
 */
public class PagePath {
    public static final String PARAMETERS_START = "?";
    public static final String EQUALS = "=";
    public static final String INDEX_PAGE = "/index.jsp";
    public static final String MAIN_PAGE = "/jsp/navigation/main.jsp";
    public static final String PRODUCT_PAGE = "/jsp/navigation/products.jsp";
    public static final String REGISTRATION_PAGE = "/jsp/navigation/registration.jsp";
    public static final String ACCOUNT_CREATION_DETAILS_PAGE = "/jsp/navigation/account_creation_details.jsp";
    /* client pages */
    public static final String ORDER_PAGE = "/jsp/navigation/client/orders.jsp";
    public static final String CART_PAGE = "/jsp/navigation/client/cart.jsp";
    public static final String SETTINGS_PAGE = "/jsp/navigation/client/settings.jsp";
    /* manager pages */
    public static final String MEAL_MANAGEMENT_PAGE = "/jsp/navigation/manager/meal_management.jsp";
    public static final String MENU_MANAGEMENT_PAGE = "/jsp/navigation/manager/menu_management.jsp";
    public static final String MENU_CREATION_PAGE = "/jsp/navigation/manager/menu_creation.jsp";
    public static final String MENU_UPDATE_PAGE = "/jsp/navigation/manager/menu_update.jsp";
    public static final String ORDER_CONFIRMATION_PAGE = "/jsp/navigation/manager/orders_confirmation.jsp";
    /* admin pages */
    public static final String USER_MANAGEMENT_PAGE = "/jsp/navigation/admin/user_management.jsp";
    /* error pages */
    public static final String ERROR_400_PAGE = "/jsp/error/error400.jsp";
    private PagePath() {
    }
}