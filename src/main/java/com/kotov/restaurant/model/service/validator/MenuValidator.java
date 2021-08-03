package com.kotov.restaurant.model.service.validator;

public class MenuValidator {
    private MenuValidator() {
    }

    public static boolean areMenuParametersValid(String title, String type, String[] mealsId) {
        return title != null && !title.isBlank()
                && type != null && !type.isBlank()
                && mealsId != null;
    }
}