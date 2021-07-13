package com.kotov.restaurant.command;

public class ParamName {
    public static final String COMMAND = "command";
    public static final String LOCALE = "locale";

    public static final String USER = "user";
    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String EMAIL = "email_address";
    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String REGISTERED = "registered";
    public static final String ADDRESS_ID = "address_id";
    public static final String ROLE_ID = "role_id";
    public static final String STATUS_ID = "status_id";

    public static final String REGISTRATION_DATA = "registration_data";

    public static final int LOGIN_INDEX = 0;
    public static final int PASSWORD_INDEX = 1;
    public static final int CONFIRM_PASSWORD_INDEX = 2;
    public static final int EMAIL_INDEX = 3;
    public static final int MOBILE_NUMBER_INDEX = 4;

    public static final String LOGIN_REGEX = "[A-Za-z]\\w{2,20}";
    public static final String PASSPORT_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,24}$";
    public static final String EMAIL_REGEX = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    public static final String MOBILE_NUMBER_REGEX = "(25|29|33|44)\\d{7}";

    public static final String LOGIN_ERROR = "login_error";
    public static final String INVALID_LOGIN_MESSAGE = "invalid_login";
    public static final String NOT_UNIQUE_LOGIN_MESSAGE = "not_unique_login";
    public static final String PASSPORT_ERROR = "passport_error";
    public static final String INVALID_PASSPORT_MESSAGE = "invalid_passport";
    public static final String PASSWORD_MISMATCH_MESSAGE = "password_mismatch";
    public static final String EMAIL_ERROR = "email_error";
    public static final String INVALID_EMAIL_MESSAGE = "invalid_email";
    public static final String NOT_UNIQUE_EMAIL_MESSAGE = "not_unique_email";
    public static final String MOBILE_NUMBER_ERROR = "mobile_number_error";
    public static final String INVALID_MOBILE_NUMBER_MESSAGE = "invalid_mobile_number";
    public static final String NOT_UNIQUE_MOBILE_NUMBER_MESSAGE = "not_unique_mobile_number";

    public static final int FIRST_PARAM_INDEX = 1;
    public static final int SECOND_PARAM_INDEX = 2;
    public static final int THIRD_PARAM_INDEX = 3;
    public static final int FOURTH_PARAM_INDEX = 4;
    public static final int FIFTH_PARAM_INDEX = 5;
    public static final int SIXTH_PARAM_INDEX = 6;
    public static final int SEVENTH_PARAM_INDEX = 7;

    public static final String VALID = "true";
    public static final String INVALID = "false";

    private ParamName() {
    }
}