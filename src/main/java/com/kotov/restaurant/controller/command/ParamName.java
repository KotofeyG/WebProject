package com.kotov.restaurant.controller.command;

public class ParamName {
    public static final String DELIVERY_TIME = "delivery_time";

    /* user registration params */
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String EMAIL = "email_address";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String USER_ROLE = "role";

    /* user registration check params */
    public static final String TRUE = "true";
    public static final String INVALID_LOGIN_MESSAGE = "invalidLogin";
    public static final String INVALID_PASSPORT_MESSAGE = "invalidPassport";
    public static final String INVALID_EMAIL_MESSAGE = "invalidEmail";
    public static final String INVALID_MOBILE_NUMBER_MESSAGE = "invalidMobileNumber";
    public static final String NOT_UNIQUE_LOGIN_MESSAGE = "notUniqueLogin";
    public static final String NOT_UNIQUE_EMAIL = "notUniqueEmail";
    public static final String NOT_UNIQUE_MOBILE_NUMBER = "notUniqueMobileNumber";

    /* user personal data params */
    public static final String ADDRESS = "address";
    public static final String PERSONAL_DATA = "personal_data";
    public static final String OLD_PASSWORD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CITY = "city";
    public static final String STREET = "street";
    public static final String BUILDING = "building";
    public static final String BUILDING_BLOCK = "block";
    public static final String FLAT = "flat";
    public static final String ENTRANCE = "entrance";
    public static final String FLOOR = "floor";
    public static final String INTERCOM_CODE = "intercom_code";
    public static final String DISCOUNT_CARD = "discount_card";
    public static final String FIRST_NAME = "first_name";
    public static final String PATRONYMIC = "patronymic";
    public static final String LAST_NAME = "last_name";

    public static final String CARD_TYPE = "card_type";
    public static final String CARD_NUMBER = "card_number";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String HOLDER_NAME = "holder_name";
    public static final String CVC = "cvc";

    /* session attributes */
    public static final String CURRENT_PAGE = "current_page";
    public static final String LOCALE = "locale";

    /* user-meal action params */
    public static final String ACTION = "action";
    public static final String BLOCK = "block";
    public static final String UNBLOCK = "unblock";
    public static final String DELETE = "delete";
    public static final String SELECTED = "selected";
    public static final String OFFLINE = "OFFLINE";
    public static final String BLOCKED = "BLOCKED";
    public static final boolean ACTIVE = true;
    public static final boolean NOT_ACTIVE = false;

    /* adding new meal-menu */
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String PRICE = "price";
    public static final String RECIPE = "recipe";
    public static final String IMAGE = "image";

    /* general params */
    public static final String COMMAND = "command";

    /* product params */
    public static final String COMMON = "";
    public static final String PRODUCT = "product";
    public static final String MEAL_NUMBER = "meal_number";
    public static final String MEAL_ID = "meal_id";

    public static final String ALL_MEALS = "meals";
    public static final String ALL_MENUS = "menus";

    public static final String MENU = "menu";

    public static final String REMOVE = "remove";
    public static final String APPEND = "append";
    public static final String SELECTED_MENU = "selected_menu";

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final String USER = "user";

    public static final String MEAL_CREATION_DATA = "meal_creation_data";

    private ParamName() {
    }
}