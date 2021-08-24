package com.kotov.restaurant.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuValidator {
    private static final String STREET_PATTERN = "[\\p{Punct}&&[^-]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final Pattern pattern = Pattern.compile(STREET_PATTERN);

    private MenuValidator() {
    }

    public static boolean isTitleValid(String title) {
        Matcher matcher = pattern.matcher(title);
        return !matcher.find() && !title.isBlank();
    }

    public static boolean isTypeValid(String type) {
        return type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);
    }
}