package com.kotov.restaurant.model.service.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {                                                                // check regexes
    private static final String LOGIN_REGEX = "[A-Za-z]\\w{2,20}";
    private static final String PASSPORT_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,24}$";
    private static final String NAME_PATTERN = "[\\p{Punct}&&[^-]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String EMAIL_REGEX = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
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
        Matcher matcher = Pattern.compile(NAME_PATTERN).matcher(name);
        return name != null && (name.isEmpty() || !matcher.find());
    }

    public static boolean isEmailValid(String email) {
        return email != null && (email.isEmpty() || email.matches(EMAIL_REGEX));
    }

    public static boolean isMobileNumberValid(String mobileNumber) {
        return mobileNumber != null && ( mobileNumber.isEmpty() || mobileNumber.matches(MOBILE_NUMBER_REGEX));
    }
}