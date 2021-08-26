package com.kotov.restaurant.util.validator;

import com.kotov.restaurant.model.entity.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidator {
    private static final String WRONG_SYMBOL_LOCATION_PATTERN = "-{2,}| {2,}|^[\\- ]|[\\- ]$";
    private static final Pattern pattern = Pattern.compile(WRONG_SYMBOL_LOCATION_PATTERN);
    private static final String STREET_PATTERN = "[А-Яа-яA-Za-z\\d- ]{3,30}";
    private static final String BUILDING_PATTERN = "\\d{1,3}";
    private static final String BLOCK_PATTERN = "\\d{1,2}|[А-Яа-яA-Za-z]";
    private static final String FLAT_PATTERN = "\\d{1,4}";
    private static final String ENTRANCE_FLOOR_PATTERN = "\\d{1,2}";
    private static final String INTERCOM_CODE_PATTERN = "\\w{1,12}";

    private AddressValidator() {
    }

    public static boolean isCityValid(String city) {
        boolean result = false;
        if (city != null) {
            for (Address.City value : Address.City.values()) {
                if (value.toString().equals(city.toUpperCase())) {
                    result = true;
                }
            }
        }
        return result;
    }

    public static boolean isStreetValid(String street) {
        boolean result = street != null && !street.isBlank() && street.matches(STREET_PATTERN);
        if (result) {
            Matcher matcher = pattern.matcher(street);
            result = !matcher.find();
        }
        return result;
    }

    public static boolean isBuildingValid(String building) {
        return building != null && building.matches(BUILDING_PATTERN);
    }

    public static boolean isBlockValid(String block) {
        return block != null && (block.isEmpty() || block.matches(BLOCK_PATTERN));
    }

    public static boolean isFlatValid(String flat) {
        return flat != null && (flat.isEmpty() || flat.matches(FLAT_PATTERN));
    }

    public static boolean isEntranceValid(String entrance) {
        return entrance != null && (entrance.isEmpty() || entrance.matches(ENTRANCE_FLOOR_PATTERN));
    }

    public static boolean isFloorValid(String floor) {
        return floor != null && (floor.isEmpty() || floor.matches(ENTRANCE_FLOOR_PATTERN));
    }

    public static boolean isIntercomCodeValid(String intercomCode) {
        return intercomCode != null && (intercomCode.isEmpty() || intercomCode.matches(INTERCOM_CODE_PATTERN));
    }
}