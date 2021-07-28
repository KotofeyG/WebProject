package com.kotov.restaurant.model.service.validator;

import java.util.Map;

import static com.kotov.restaurant.controller.command.ParamName.*;

public class MealValidator {
    private MealValidator() {
    }

    public static boolean areMealParamsValid(Map<String, String> dataCheckResult) {
        String title = dataCheckResult.get(TITLE);
        String type = dataCheckResult.get(TYPE);
        String price = dataCheckResult.get(PRICE);
        String recipe = dataCheckResult.get(RECIPE);

        return title != null && !title.isBlank()
                && type != null && !type.isBlank()
                && price != null && !price.isBlank()
                && recipe != null && !recipe.isBlank();
    }
}