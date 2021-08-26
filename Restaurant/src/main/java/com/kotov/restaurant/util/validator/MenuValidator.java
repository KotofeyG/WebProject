package com.kotov.restaurant.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuValidator {
    private static final String WRONG_SYMBOL_PATTERN = "[\\p{Punct}&&[^-,]]|-{2,}| {2,}|,{2,}|^[\\- ,]|[\\- ,]$";
    private static final String TITLE_REGEX = "[A-Za-zА-Яа-я-, ]{2,75}";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final Pattern pattern = Pattern.compile(WRONG_SYMBOL_PATTERN);

    private MenuValidator() {
    }

    public static boolean isTitleValid(String title) {
        boolean result = title != null;
        if (result) {
            Matcher matcher = pattern.matcher(title);
            result = !title.isBlank() && title.matches(TITLE_REGEX) && !matcher.find();
        }
        return result;
    }

    public static boolean isTypeValid(String type) {
        boolean result = type != null;
        if (result) {
            result = type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);
        }
        return result;
    }
}