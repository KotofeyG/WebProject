package com.kotov.restaurant.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AddressValidatorTest {
    @DataProvider(name = "city")
    public Object[][] createCities() {
        String[][] cities = {{"Minsk"}, {"Brest"}, {"Vitebsk"}, {"Gomel"}, {"Grodno"}, {"Mogilev"}};
        return cities;
    }

    @DataProvider(name = "validStreet")
    public Object[][] createValidStreet() {
        String[][] cities = {{"Ленина"}, {"60 лет Октября"}, {"Ленина-Сталина"}};
        return cities;
    }

    @DataProvider(name = "invalidStreet")
    public Object[][] createInvalidStreet() {
        String[][] cities = {{" Ленина"}, {"Ленина "}, {"-Ленина"}, {"Ленина-"}, {"Лен>ина"}, {"Лен$ина"}, {"Лен--ина"}
                , {"Лен  ина"}, {"Ян"}, {"ЛенинаЛенинаЛенинаЛенинаЛенина1"}, {"     "}};
        return cities;
    }

    @DataProvider(name = "invalidBuilding")
    public Object[][] createInvalidBuilding() {
        String[][] buildings = {{"-99"}, {"1000"}, {"33.2"}, {"Б"}, {""}};
        return buildings;
    }

    @DataProvider(name = "validBlock")
    public Object[][] createValidBlock() {
        String[][] buildings = {{"10"}, {"5"}, {"A"}, {""}};
        return buildings;
    }

    @DataProvider(name = "invalidBlock")
    public Object[][] createInvalidBlock() {
        String[][] buildings = {{"-3"}, {"333"}, {"AB"}, {" "}};
        return buildings;
    }

    @DataProvider(name = "validFlat")
    public Object[][] createValidFlat() {
        String[][] buildings = {{"9999"}, {"1"}, {""}};
        return buildings;
    }

    @DataProvider(name = "invalidFlat")
    public Object[][] createInvalidFlat() {
        String[][] buildings = {{"99999"}, {"99A"}, {"-1"}, {" "}};
        return buildings;
    }

    @DataProvider(name = "validEntranceOrFloor")
    public Object[][] createValidEntranceOrFloor() {
        String[][] buildings = {{"1"}, {"22"}, {""}};
        return buildings;
    }

    @DataProvider(name = "invalidEntranceOrFloor")
    public Object[][] createInvalidEntranceOrFloor() {
        String[][] buildings = {{"999"}, {"-9"}, {"B"}, {" "}};
        return buildings;
    }

    @DataProvider(name = "validIntercomCode")
    public Object[][] createValidIntercomCode() {
        String[][] buildings = {{"123456789012"}, {"ABCDIFG"}, {"1a2B3c4D"}, {""}};
        return buildings;
    }

    @DataProvider(name = "invalidIntercomCode")
    public Object[][] createInvalidIntercomCode() {
        String[][] buildings = {{"1234567890123"}, {"123&AdC"}, {" "}};
        return buildings;
    }

    @Test(dataProvider = "city")
    public void testIsCityValidWithTrueCondition(String city) {
        boolean condition = AddressValidator.isCityValid(city);
        assertTrue(condition, "Wrong city: " + city);
    }

    @Test()
    public void testIsCityValidWithFalseCondition() {
        String data = "Minsk1";
        boolean condition = AddressValidator.isCityValid(data);
        assertFalse(condition);
    }

    @Test()
    public void testIsCityValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isCityValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validStreet")
    public void testIsStreetValidWithTrueCondition(String street) {
        boolean condition = AddressValidator.isStreetValid(street);
        assertTrue(condition);
    }

    @Test(dataProvider = "invalidStreet")
    public void testIsStreetValidWithFalseCondition(String street) {
        boolean condition = AddressValidator.isStreetValid(street);
        assertFalse(condition, "The value " + street + " is valid");
    }

    @Test
    public void testIsStreetValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isStreetValid(data);
        assertFalse(condition);
    }

    @Test
    public void testIsBuildingValidWithTrueCondition() {
        String data = "999";
        boolean condition = AddressValidator.isBuildingValid(data);
        assertTrue(condition, "The value " + data + " is invalid");
    }

    @Test(dataProvider = "invalidBuilding")
    public void testIsBuildingValidWithFalseCondition(String building) {
        boolean condition = AddressValidator.isBuildingValid(building);
        assertFalse(condition, "The value " + building + " is valid");
    }

    @Test
    public void testIsBuildingValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isBuildingValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validBlock")
    public void testIsBlockValidWithTrueCondition(String block) {
        boolean condition = AddressValidator.isBlockValid(block);
        assertTrue(condition, "The value " + block + " is invalid");
    }

    @Test(dataProvider = "invalidBlock")
    public void testIsBlockValidWithFalseCondition(String block) {
        boolean condition = AddressValidator.isBlockValid(block);
        assertFalse(condition, "The value " + block + " is valid");
    }

    @Test
    public void testIsBlockValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isBlockValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validFlat")
    public void testIsFlatValidWithTrueCondition(String flat) {
        boolean condition = AddressValidator.isFlatValid(flat);
        assertTrue(condition, "The value " + flat + " is invalid");
    }

    @Test(dataProvider = "invalidFlat")
    public void testIsFlatValidWithFalseCondition(String flat) {
        boolean condition = AddressValidator.isFlatValid(flat);
        assertFalse(condition, "The value " + flat + " is valid");
    }

    @Test
    public void testIsFlatValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isFlatValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validEntranceOrFloor")
    public void testIsEntranceValidWithTrueCondition(String entrance) {
        boolean condition = AddressValidator.isEntranceValid(entrance);
        assertTrue(condition, "The value " + entrance + " is invalid");
    }

    @Test(dataProvider = "invalidEntranceOrFloor")
    public void testIsEntranceValidWithFalseCondition(String entrance) {
        boolean condition = AddressValidator.isEntranceValid(entrance);
        assertFalse(condition, "The value " + entrance + " is valid");
    }

    @Test
    public void testIsEntranceValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isEntranceValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validEntranceOrFloor")
    public void testIsFloorValidWithTrueCondition(String floor) {
        boolean condition = AddressValidator.isFloorValid(floor);
        assertTrue(condition, "The value " + floor + " is invalid");
    }

    @Test(dataProvider = "invalidEntranceOrFloor")
    public void testIsFloorValidWithFalseCondition(String floor) {
        boolean condition = AddressValidator.isFloorValid(floor);
        assertFalse(condition, "The value " + floor + " is valid");
    }

    @Test
    public void testIsFloorValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isFloorValid(data);
        assertFalse(condition);
    }

    @Test(dataProvider = "validIntercomCode")
    public void testIsIntercomCodeValidWithTrueCondition(String intercomCode) {
        boolean condition = AddressValidator.isIntercomCodeValid(intercomCode);
        assertTrue(condition, "The value " + intercomCode + " is invalid");
    }

    @Test(dataProvider = "invalidIntercomCode")
    public void testIsIntercomCodeValidWithFalseCondition(String intercomCode) {
        boolean condition = AddressValidator.isIntercomCodeValid(intercomCode);
        assertFalse(condition, "The value " + intercomCode + " is valid");
    }

    @Test
    public void testIsIntercomCodeValidWithNullCondition() {
        String data = null;
        boolean condition = AddressValidator.isIntercomCodeValid(data);
        assertFalse(condition);
    }
}