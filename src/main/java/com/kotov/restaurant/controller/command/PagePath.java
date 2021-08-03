package com.kotov.restaurant.controller.command;

public class PagePath {
    /* general pages */
    public static final String INDEX_PAGE = "/index.jsp";
    public static final String MAIN_PAGE = "/jsp/navigation/main.jsp";
    public static final String ADMIN_MAIN_PAGE = "/jsp/navigation/admin_main.jsp";
    public static final String REGISTRATION_PAGE = "/jsp/navigation/registration.jsp";
    public static final String ACCOUNT_CREATION_DETAILS_PAGE = "/jsp/navigation/account_creation_details.jsp";

    /* product pages */
    public static final String ROLL_PAGE = "/jsp/navigation/rolls.jsp";

    /* client pages */
    public static final String MENU_PAGE = "/jsp/navigation/client/menu.jsp";
    public static final String ORDERS_PAGE = "/jsp/navigation/client/orders.jsp";
    public static final String BOOKING_PAGE = "/jsp/navigation/client/booking.jsp";
    public static final String SETTINGS_PAGE = "/jsp/navigation/client/settings.jsp";
    public static final String REVIEWS_PAGE = "/jsp/navigation/client/reviews.jsp";
    public static final String CART_PAGE = "/jsp/navigation/client/cart.jsp";

    /* manager pages */
    public static final String MENU_EDITING_PAGE = "/jsp/navigation/manager/menu_editing.jsp";
    public static final String BILL_CREATION_PAGE = "/jsp/navigation/manager/bill_creation.jsp";
    public static final String ORDER_CONFIRMATION_PAGE = "/jsp/navigation/manager/orders_confirmation.jsp";
    public static final String MEAL_MANAGEMENT_PAGE = "/jsp/navigation/manager/meal_management.jsp";
    public static final String MENU_MANAGEMENT_PAGE = "/jsp/navigation/manager/menu_management.jsp";
    public static final String MENU_CREATION_PAGE = "/jsp/navigation/manager/menu_creation.jsp";
    public static final String MENU_UPDATE_PAGE = "/jsp/navigation/manager/menu_update.jsp";

    /* admin pages */
    public static final String USER_MANAGEMENT_PAGE = "/jsp/navigation/admin/user_management.jsp";
    public static final String REVIEW_MANAGEMENT_PAGE = "/jsp/navigation/admin/review_management.jsp";

    /* error pages */
    public static final String INTERNAL_SERVER_ERROR_PAGE = "/jsp/error/internal_server_error.jsp";
    public static final String NON_EXISTENT_COMMAND_PAGE = "/jsp/error/non_existent_command.jsp";

    private PagePath() {
    }
}