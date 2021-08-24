package com.kotov.restaurant.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LocaleValidatorTest {
    @DataProvider(name = "validLocale")
    public Object[][] createCities() {
        String[][] cities = {{"ru_RU"}, {"en_US"}};
        return cities;
    }

    @Test(dataProvider = "validLocale")
    public void testIsLocaleExistWithTrueCondition(String locale) {
        boolean condition = LocaleValidator.isLocaleExist(locale);
        assertTrue(condition, "Locale " + locale + " doesn't exist in this application");
    }

    @Test
    public void testIsLocaleExistWithFalseCondition() {
        String data = "de_DE";
        boolean condition = LocaleValidator.isLocaleExist(data);
        assertFalse(condition);
    }

    @Test
    public void testIsLocaleExistWithNullCondition() {
        String data = null;
        boolean condition = LocaleValidator.isLocaleExist(data);
        assertFalse(condition);
    }
}