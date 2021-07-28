package com.kotov.restaurant.controller.command;

import java.io.File;

public class ParamName {
    /* user registration params */
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String EMAIL = "email_address";
    public static final String MOBILE_NUMBER = "mobile_number";

    /* user registration check params */
    public static final String TRUE = "true";
    public static final String INVALID_LOGIN_MESSAGE = "invalidLogin";
    public static final String INVALID_PASSPORT_MESSAGE = "invalidPassport";
    public static final String INVALID_EMAIL_MESSAGE = "invalidEmail";
    public static final String INVALID_MOBILE_NUMBER_MESSAGE = "invalidMobileNumber";
    public static final String NOT_UNIQUE_LOGIN_MESSAGE = "notUniqueLogin";
    public static final String NOT_UNIQUE_EMAIL_MESSAGE = "notUniqueEmail";
    public static final String NOT_UNIQUE_MOBILE_NUMBER_MESSAGE = "notUniqueMobileNumber";

    /* session attributes */
    public static final String CURRENT_PAGE = "current_page";
    public static final String LOCALE = "locale";

    /* general params */
    public static final String COMMAND = "command";
    public static final String RUSSIAN_LANGUAGE = "ru-RU";
    public static final String ENGLISH_LANGUAGE = "en-US";

    public static final String RESULT = "result";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static final boolean ACTIVE = true;
    public static final boolean NOT_ACTIVE = false;

    public static final String ACTION = "action";
    public static final String ACTION_BLOCK = "block";
    public static final String ACTION_UNBLOCK = "unblock";
    public static final String ACTION_DELETE = "delete";

    public static final String MENU_UPDATE = "menu_update";
    public static final String MENU_CREATION = "menu_creation";
    public static final String MENU_MANAGEMENT = "menu_management";
    public static final String MEAL_MANAGEMENT = "meal_management";

    public static final String ALL_MEALS = "meals";
    public static final String ALL_MENUS = "menus";

    public static final String MENU = "menu";

    public static final String REMOVE = "remove";
    public static final String APPEND = "append";

    public static final String PATH_TO_IMAGES = String.join(File.separator, "C:", "Users", "Lenovo"
            , "IdeaProjects", "Restaurant", "src", "main", "webapp", "images", "meals") + "\\";
    public static final char DOT = '.';
    public static final String EMPTY = "";
    public static final String FORMAT_JPG = "jpg";

    public static final String SELECTED = "selected";



    public static final String VALID = "valid";
    public static final String INVALID = "invalid";


    public static final String NOT_UNIQUE = "not_unique";




    public static final int INTERNAL_SERVER_ERROR = 500;

    /* input registration params */
    public static final String USER = "user";
    public static final String ID = "id";


    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";

    public static final String REGISTERED = "registered";
    public static final String ADDRESS_ID = "address_id";
    public static final String ROLE_ID = "role_id";
    public static final String STATUS_ID = "status_id";

    /* adding new meal */
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String PRICE = "price";
    public static final String RECIPE = "recipe";
    public static final String IMAGE = "image";


    public static final String MEAL_CREATION_DATA = "meal_creation_data";

    public static final String VALID_MEAL = "valid_meal";
    public static final String MEAL_ADDED = "meal_added";
    public static final String INVALID_MEAL = "meal_error";
    public static final String NOT_UNIQUE_MEAL_TITLE = "not_unique_meal";
    public static final String INVALID_MEAL_REGISTERED_FIELDS = "invalid_fields";
    public static final int FIRST_POSITION = 1;

    /* adding new menu */
    public static final String MENU_CREATION_DATA = "menu_creation_data";

    public static final String VALID_MENU = "valid_menu";
    public static final String MENU_ADDED = "menu_added";
    public static final String MENU_ERROR = "menu_error";
    public static final String NOT_UNIQUE_MENU = "not_unique_menu";





    /* sign in error message */
    public static final String INVALID_LOGIN_OR_PASSWORD = "invalid_login_or_password";



    private ParamName() {
    }
}