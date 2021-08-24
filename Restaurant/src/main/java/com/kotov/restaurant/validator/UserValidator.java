package com.kotov.restaurant.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final String LOGIN_REGEX = "[A-Za-z]\\w{1,19}";
    private static final String PASSPORT_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\w+$).{8,24}$";
    private static final String WRONG_SYMBOL_PATTERN = "[\\p{Punct}&&[^-]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String NAME_PATTERN = "[A-Za-zА-Я-а-я-]{2,40}";
    private static final String EMAIL_REGEX = "^([A-Za-z0-9_-]+\\.)*[A-Za-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    private static final String EMAIL_SYMBOL_NUMBER = ".{8,50}";
    private static final String MOBILE_NUMBER_REGEX = "(25|29|33|44)\\d{7}";

    private UserValidator() {
    }

    public static boolean isLoginValid(String login) {
        return login != null && login.matches(LOGIN_REGEX);
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSPORT_REGEX);
    }

    public static boolean isNameValid(String name) {
        boolean result = name != null;
        if (result) {
            Matcher matcher = Pattern.compile(WRONG_SYMBOL_PATTERN).matcher(name);
            return name != null && !name.isBlank() && !matcher.find() && name.matches(NAME_PATTERN);
        }
        return result;
    }

    public static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX) && email.matches(EMAIL_SYMBOL_NUMBER);
    }

    public static boolean isMobileNumberValid(String mobileNumber) {
        return mobileNumber != null && mobileNumber.matches(MOBILE_NUMBER_REGEX);
    }
}