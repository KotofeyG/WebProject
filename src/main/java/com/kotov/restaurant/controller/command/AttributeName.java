package com.kotov.restaurant.controller.command;

public class AttributeName {
    /* user registration attributes */
    public static final String VALID_LOGIN = "valid_login";
    public static final String VALID_EMAIL = "valid_email";
    public static final String VALID_MOBILE_NUMBER = "valid_mobile_number";
    public static final String INVALID_LOGIN = "invalid_login";
    public static final String INVALID_PASSPORT = "invalid_passport";
    public static final String INVALID_EMAIL = "invalid_email";
    public static final String INVALID_MOBILE_NUMBER = "invalid_mobile_number";
    public static final String INVALID_MESSAGE = "invalid_message";
    public static final String NOT_UNIQUE_MESSAGE = "not_unique_message";
    public static final String PASSWORD_MISMATCH_MESSAGE = "password_mismatch";

    /* user authentication attributes */
    public static final String USER_ATTR = "user";
    public static final String AUTHORIZATION_DATA = "authorization_data";

    /* user management attributes */
    public static final String ALL_USERS = "users";
    public static final String USER_ACTION_RESULT = "result";
    public static final String USER_SEARCH_RESULT = "user_search_result";

    /* meal management attributes */
    public static final String VALID = "valid";
    public static final String INVALID = "invalid";
    public static final String NOT_UNIQUE = "not_unique";
    public static final String MEAL_SEARCH_RESULT = "meal_search_result";
    public static final String MEAL_ACTION_RESULT = "meal_action_result";

    /* menu management attributes */
    public static final String MENU_SEARCH_RESULT = "menu_search_result";
    public static final String UNSELECTED_MENU = "unselected_menu";
    public static final String MENU_DELETE_RESULT = "menu_delete_result";

    /* adding new menu */
    public static final String MENU_CREATION_RESULT = "menu_creation_result";

    /* application attributes */
    public static final String ROLLS = "ROLLS";

    private AttributeName() {
    }
}