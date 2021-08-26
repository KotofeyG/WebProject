package com.kotov.restaurant.util.validator;

public class TimeValidator {
    private static final String TIME_REGEX = "(([01]\\d)|(2[123])):[012345]\\d";

    private TimeValidator() {
    }

    public static boolean isTimeValid(String time) {
        return time != null && time.matches(TIME_REGEX);
    }
}