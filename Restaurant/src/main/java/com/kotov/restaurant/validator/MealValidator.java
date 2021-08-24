package com.kotov.restaurant.validator;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealValidator {
    private static final String WRONG_SYMBOL_PATTERN = "[\\p{Punct}&&[^-,]]|-{2,}| {2,}|,{2,}|^[\\- ,]|[\\- ,]$";
    private static final String TITLE_REGEX = "[A-Za-zА-Яа-я-, ]{2,75}";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final String PRICE_REGEX = "\\d{1,5}|\\d{1,5}\\.\\d{1,2}";
    private static final Pattern pattern = Pattern.compile(WRONG_SYMBOL_PATTERN);

    private MealValidator() {
    }

    public static boolean areMealParamsValid(Map<String, String> dataCheckResult, InputStream image) {
        String title = dataCheckResult.get(TITLE);
        String type = dataCheckResult.get(TYPE);
        String price = dataCheckResult.get(PRICE);
        String recipe = dataCheckResult.get(RECIPE);

        boolean result = title != null && type != null && price!= null && recipe != null && image != null;
        if (result) {
            Matcher titleMatcher = pattern.matcher(title);
            Matcher recipeMatcher = pattern.matcher(recipe);
            result = !title.isBlank() && title.matches(TITLE_REGEX) && !titleMatcher.find() && type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX)
                    && !recipe.isBlank() && !recipeMatcher.find() && price.matches(PRICE_REGEX);
        }
        return  result;
    }

    public static boolean isMealTypeExist(String meal) {
        return meal != null && meal.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);
    }

    public static boolean isMealInOrderExist(String[] mealIdArray, String[] mealNumberArray) {
        return mealIdArray != null && mealNumberArray != null && mealIdArray.length == mealNumberArray.length;
    }
}