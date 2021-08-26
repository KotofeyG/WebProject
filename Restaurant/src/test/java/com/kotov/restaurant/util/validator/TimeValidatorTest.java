package com.kotov.restaurant.util.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalTime;

import static org.testng.Assert.*;

public class TimeValidatorTest {

    @DataProvider(name = "validTime")
    public Object[][] createValidTime() {
        String[][] data = {{"00:59"}, {"23:59"}, {"00:00"}, {"10:00"}};
        return data;
    }

    @DataProvider(name = "invalidTime")
    public Object[][] createInvalidTime() {
        String[][] data = {{"00:60"}, {"24:00"}, {"25:00"}, {"1:59"}};
        return data;
    }

    @Test(dataProvider = "validTime")
    public void testIsTimeValidWithTrueCondition(String time) {
        boolean condition = TimeValidator.isTimeValid(time);
        assertTrue(condition, "The value " + time + " is invalid");
    }

    @Test(dataProvider = "invalidTime")
    public void testIsTimeValidWithFalseCondition(String time) {
        boolean condition = TimeValidator.isTimeValid(time);
        assertFalse(condition, "The value " + time + " is valid");
    }

    @Test
    public void testIsTimeValidWithNullCondition() {
        String time = null;
        boolean condition = TimeValidator.isTimeValid(time);
        assertFalse(condition, "The value isn't null");
    }
}