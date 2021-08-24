package com.kotov.restaurant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String WRONG_SYMBOL_PATTERN = "[\\p{Punct}&&[^-,]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String AVAILABLE_TYPE_OF_MEALS_REGEX = "roll|nigiri|sashimi|soup|main_dish|salad|appetizer";
    private static final String PRICE_REGEX = "\\d{1,5}|\\d{1,5}\\.\\d{1,2}";
    private static final Pattern pattern = Pattern.compile(WRONG_SYMBOL_PATTERN);

    public static void main(String[] args) throws InterruptedException {
        String x = "Суши на понедельник";
        Matcher matcher = pattern.matcher(x);
        boolean result = !x.isBlank() && !matcher.find();

        String type = "roll";
        result &=type.matches(AVAILABLE_TYPE_OF_MEALS_REGEX);

        String recipe = "лосось, сливочный сыр, омлетный блинчик, икра лосося, рис, нори";

//        result &= !recipe.isBlank() && !matcher.reset(recipe).find();

        result = !matcher.reset(recipe).find();

        System.out.println(result);
    }
}