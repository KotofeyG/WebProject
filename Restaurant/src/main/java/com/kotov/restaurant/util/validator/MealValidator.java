package com.kotov.restaurant.util.validator;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealValidator {
    private static final String WRONG_SYMBOL_PATTERN = "[\\p{Punct}&&[^-,]]|-{2,}| {2,}|,{2,}|^[\\- ,]|[\\- ,]$";
    private static final Pattern pattern = Pattern.compile(WRONG_SYMBOL_PATTERN);
    private static final String TITLE_REGEX = "[A-Za-zА-Яа-я-, ]{2,75}";
    private static final String RECIPE_REGEX = "[A-Za-zА-Яа-я-, ]{2,200}";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final String PRICE_REGEX = "\\d{1,5}|\\d{1,5}\\.\\d{1,2}";
    private static final String NUMBER_REGEX = "\\d+";

    private MealValidator() {
    }

    public static boolean areMealParamsValid(Map<String, String> dataCheckResult, InputStream image) {
        boolean result = dataCheckResult != null;
        if (result) {
            String title = dataCheckResult.get(TITLE);
            String type = dataCheckResult.get(TYPE);
            String price = dataCheckResult.get(PRICE);
            String recipe = dataCheckResult.get(RECIPE);
            result = title != null && type != null && price != null && recipe != null && image != null;
            if (result) {
                Matcher titleMatcher = pattern.matcher(title);
                Matcher recipeMatcher = pattern.matcher(recipe);
                result = !title.isBlank() && title.matches(TITLE_REGEX) && !titleMatcher.find() &&
                        type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX) && !recipe.isBlank() && recipe.matches(RECIPE_REGEX)
                        && !recipeMatcher.find() && price.matches(PRICE_REGEX);
            }
        }
        return  result;
    }

    public static boolean isMealTypeValid(String meal) {
        return meal != null && meal.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);
    }

    public static boolean isMealInOrderExist(String[] mealIdArray, String[] mealNumberArray) {
        boolean result = mealIdArray != null && mealNumberArray != null && mealIdArray.length == mealNumberArray.length;
        if (result) {
            int counter = 0;
            while (counter < mealIdArray.length) {
                result = mealIdArray[counter].matches(NUMBER_REGEX) && mealNumberArray[counter].matches(NUMBER_REGEX);
                if (!result) {
                    break;
                }
                counter++;
            }
        }
        return result;
    }
}