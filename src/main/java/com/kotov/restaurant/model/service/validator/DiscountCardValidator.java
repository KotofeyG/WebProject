package com.kotov.restaurant.model.service.validator;

public class DiscountCardValidator {
    private static final String NUMBER_PATTERN = "\\d{16}";

    private DiscountCardValidator() {
    }

    public static boolean isCardNumberValid(String number) {
        return number != null && number.matches(NUMBER_PATTERN);
    }
}