package com.kotov.restaurant.controller.command;

public class AttributeName {
    public static final String PAGINATION_ITEM = "pagination_item";
    public static final String WRONG_COMMAND = "wrong_command";
    public static final String RESULT_OF_INSERT = "result_of_insert";


    public static final String ADDRESS_ADDING_RESULT = "address_adding_result";
    public static final String DISCOUNT_CARD_ADDING_RESULT = "discount_card_adding_result";
    public static final String PERSONAL_DATA_CHANGE_RESULT = "personal_data_change_result";
    public static final String PASSWORD_CHANGE_RESULT = "password_change_result";

    /* user registration attributes */
    public static final String VALID_LOGIN = "valid_login";
    public static final String VALID_EMAIL = "valid_email";
    public static final String VALID_MOBILE_NUMBER = "valid_mobile_number";
    public static final String INVALID_LOGIN = "invalid_login";
    public static final String INCORRECT_PASSWORD = "incorrect_passport";
    public static final String INVALID_PASSPORT = "invalid_passport";
    public static final String INVALID_EMAIL = "invalid_email";
    public static final String INVALID_MOBILE_NUMBER = "invalid_mobile_number";
    public static final String INCORRECT_MESSAGE = "incorrect_message";
    public static final String INVALID_MESSAGE = "invalid_message";
    public static final String NOT_UNIQUE_MESSAGE = "not_unique_message";
    public static final String PASSWORD_MISMATCH = "password_mismatch";

    /* user authentication attributes */
    public static final String SESSION_USER = "user";
    public static final String AUTHENTICATION_RESULT = "authentication_result";

    /* user setting attributes */
    public static final String FIRST_NAME_CHANGE_RESULT = "first_name_change_result";
    public static final String PATRONYMIC_CHANGE_RESULT = "patronymic_change_result";
    public static final String LAST_NAME_CHANGE_RESULT = "last_name_change_result";
    public static final String MOBILE_NUMBER_CHANGE_RESULT = "mobile_number_change_result";
    public static final String EMAIL_CHANGE_RESULT = "email_change_result";

    public static final String VALID_CITY = "valid_city";
    public static final String VALID_STREET = "valid_street";
    public static final String VALID_BUILDING = "valid_building";
    public static final String VALID_BUILDING_BLOCK = "valid_block";
    public static final String VALID_FLAT = "valid_flat";
    public static final String VALID_ENTRANCE = "valid_entrance";
    public static final String VALID_FLOOR = "valid_floor";
    public static final String VALID_INTERCOM_CODE = "valid_intercom_code";
    public static final String VALID_FIRST_NAME = "valid_first_name";
    public static final String VALID_PATRONYMIC = "valid_patronymic";
    public static final String VALID_LAST_NAME = "valid_last_name";

    public static final String INVALID_CITY = "invalid_city";
    public static final String INVALID_STREET = "invalid_street";
    public static final String INVALID_BUILDING = "invalid_building";
    public static final String INVALID_BUILDING_BLOCK = "invalid_block";
    public static final String INVALID_FLAT = "invalid_flat";
    public static final String INVALID_ENTRANCE = "invalid_entrance";
    public static final String INVALID_FLOOR = "invalid_floor";
    public static final String INVALID_INTERCOM_CODE = "invalid_intercom_code";
    public static final String INVALID_FIRST_NAME = "invalid_first_name";
    public static final String INVALID_PATRONYMIC = "invalid_patronymic";
    public static final String INVALID_LAST_NAME = "invalid_last_name";
    public static final String ADDRESS_LIST = "addresses";

    /* user management attributes */
    public static final String USER_LIST = "users";
    public static final String USER_ACTION_RESULT = "user_action_result";
    public static final String USER_SEARCH_RESULT = "user_search_result";

    /* meal management attributes */
    public static final String VALID = "valid";
    public static final String INVALID = "invalid";
    public static final String NOT_UNIQUE = "not_unique";
    public static final String MEAL_SEARCH_RESULT = "meal_search_result";
    public static final String MEAL_ACTION_RESULT = "meal_action_result";
    public static final String MEAL_LIST = "meals";
    public static final String MEAL_CREATION_RESULT = "meal_creation_result";

    /* menu management attributes */
    public static final String MENU_SEARCH_RESULT = "menu_search_result";
    public static final String MENU_ACTION_RESULT = "menu_action_result";
    public static final String MENU_DELETE_RESULT = "menu_delete_result";
    public static final String MARKED_MEALS = "marked_meals";
    public static final String CURRENT_MENU = "current_menu";
    public static final String MENU_LIST = "menus";

    /* adding new menu */
    public static final String MENU_CREATION_RESULT = "menu_creation_result";

    /* product attributes */
    public static final String VIEW = "view";
    public static final String CART = "cart";
    public static final String CURRENT_PRODUCT_TYPE = "current_product_type";
    public static final String WRONG_PARAMETER = "wrong_parameter";
    public static final String ZERO_NUMBER_OF_MEALS = "zero_number_of_meals";

    /* order attributes */
    public static final String ORDER_LIST = "orders";
    public static final String CURRENT_ORDER = "order";
    public static final String ACTION_RESULT = "action_result";
    public static final String ZERO_NUMBER_OF_ORDERS = "zero_number_of_orders";

    /* application attributes */
    public static final String ROLLS = "ROLLS";

    /* session attributes */
    public static final String CURRENT_PAGE = "current_page";
    public static final String SESSION_LOCALE = "locale";

    public static final String WRONG_LOCALE = "wrong_locale";

    private AttributeName() {
    }
}