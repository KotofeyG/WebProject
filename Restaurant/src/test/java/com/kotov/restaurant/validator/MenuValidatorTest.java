package com.kotov.restaurant.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MenuValidatorTest {

    @DataProvider(name = "titleData")
    public Object[][] createValidTitleData() {
        String[][] data = {{""}, {" Monday"}, {"Monday "}, {"-Monday"}, {"Monday-"}, {"Mon--day"}, {"Mon  day"}
                , {"Mo$day"}, {",Monday"}, {"Monday,"}, {"Monday Monday Monday Monday Monday Monday Monday" +
                "Monday Monday Monday Monday Monday"}};
        return data;
    }

    @DataProvider(name = "menuType")
    public Object[][] createMenuType() {
        String[][] mealType = {{"roll"}, {"nigiri"}, {"sashimi"}, {"soup"}, {"main_dish"}, {"salad"}, {"appetizer"}};
        return mealType;
    }

    @Test
    public void testIsTitleValidWithTrueCondition() {
        String data = "Monday";
        boolean condition = MenuValidator.isTitleValid(data);
        assertTrue(condition, "The value " + data + " isn't valid");
    }

    @Test(dataProvider = "titleData")
    public void testIsTitleValidWithFalseCondition(String title) {
        boolean condition = MenuValidator.isTitleValid(title);
        assertFalse(condition, "The value " + title + " is valid");
    }

    @Test
    public void testIsTitleValidWithNullCondition() {
        String title = null;
        boolean condition = MenuValidator.isTitleValid(title);
        assertFalse(condition, "The value " + title + " isn't null");
    }

    @Test(dataProvider = "menuType")
    public void testIsTypeValidWithTrueCondition(String type) {
        boolean condition = MenuValidator.isTypeValid(type);
        assertTrue(condition, "Menu type " + type + " doesn't exist in this application");
    }

    @Test
    public void testIsTypeValidWithFalseCondition() {
        String type = "rolls";
        boolean condition = MenuValidator.isTypeValid(type);
        assertFalse(condition, "Menu type " + type + " exists in this application");
    }

    @Test
    public void testIsTypeValidWithNullCondition() {
        String type = null;
        boolean condition = MenuValidator.isTypeValid(type);
        assertFalse(condition, "Menu type " + type + " isn't null");
    }
}