package com.kotov.restaurant.validator;

import java.io.InputStream;
import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealValidator {
    private static final String STREET_PATTERN = "[\\p{Punct}&&[^-]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final String PRICE_REGEX = "\\d{1,5}|\\d{1,5},\\d{1,2}";

    private MealValidator() {
    }

    public static boolean areMealParamsValid(Map<String, String> dataCheckResult, InputStream image) {
        String title = dataCheckResult.get(TITLE);
        String type = dataCheckResult.get(TYPE);
        String price = dataCheckResult.get(PRICE);
        String recipe = dataCheckResult.get(RECIPE);

        return title != null && !title.isBlank()
                && type != null && type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX)
                && price != null && price.matches(PRICE_REGEX)
                && recipe != null && !recipe.isBlank()
                && image != null;
    }

    public static boolean isMealTypeExist(String meal) {
        return meal != null && meal.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);
    }
}