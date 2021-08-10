package com.kotov.restaurant.model.service.validator;

import com.kotov.restaurant.model.entity.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidator {
    private static final String STREET_PATTERN = "[\\p{Punct}&&[^-]]|-{2,}| {2,}|^[- ]|[- ]$";
    private static final String BUILDING_PATTERN = "\\d{1,3}";
    private static final String BLOCK_PATTERN = "\\d{1,2}|[А-Яа-я]";
    private static final String FLAT_PATTERN = "\\d{1,4}";
    private static final String ENTRANCE_FLOOR_PATTERN = "\\d{1,2}";
    private static final String INTERCOM_CODE_PATTERN = "\\w{1,12}";

    private AddressValidator() {
    }

    public static boolean isCityValid(String city) {
        boolean result = false;
        for (Address.City next : Address.City.values()) {
            if (next.getRussianName().equals(city)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isStreetValid(String street) {
        Matcher matcher = Pattern.compile(STREET_PATTERN).matcher(street);
        return street != null && !matcher.find();
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