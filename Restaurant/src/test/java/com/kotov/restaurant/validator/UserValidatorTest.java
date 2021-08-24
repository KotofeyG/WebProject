package com.kotov.restaurant.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UserValidatorTest {

    @DataProvider(name = "loginData")
    public Object[][] createInvalidLogin() {
        String[][] data = {{"1root"}, {"ro<o>t"}, {"main root"}, {"A"}, {"mainRootMainRootRoot1"}};
        return data;
    }

    @DataProvider(name = "passwordData")
    public Object[][] createInvalidPassword() {
        String[][] data = {{"<script>alert(\"123\")</script>"}, {"33333333"}, {"9999999"}, {"333333DD"}, {"kotovDGG"}
        , {"-33333Dd"}, {"33333<>Dg"}};
        return data;
    }

    @DataProvider(name = "nameData")
    public Object[][] createInvalidName() {
        String[][] data = {{""}, {" "}, {"I"}, {"DeN$s"}, {"Den<i>s"}, {"Den1s"}, {"DenisDenisDenisDenisDenisDenisDenisDeniss"}};
        return data;
    }

    @DataProvider(name = "emailData")
    public Object[][] createInvalidEmail() {
        String[][] data = {{"Denis-mail.ru"}, {"@mail.ru"}, {"Den<i>s@mail.ru"}, {"DenisDenisDenisDenisDenisDenisDenisDenisDenis@mail.ru"}};
        return data;
    }

    @DataProvider(name = "mobileNumberData")
    public Object[][] createInvalidMobileNumber() {
        String[][] data = {{"447181933"}, {"257181933"}, {"297181933"}, {"337181933"}};
        return data;
    }

    @Test
    public void testIsLoginValidWithTrueCondition() {
        String login = "root";
        boolean condition = UserValidator.isLoginValid(login);
        assertTrue(condition, "The value " + login + " is invalid");
    }

    @Test(dataProvider = "loginData")
    public void testIsLoginValidWithFalseCondition(String login) {
        boolean condition = UserValidator.isLoginValid(login);
        assertFalse(condition, "The value " + login + " is valid");
    }

    @Test
    public void testIsLoginValidWithNullCondition() {
        String login = null;
        boolean condition = UserValidator.isLoginValid(login);
        assertFalse(condition, "The value isn't null");
    }

    @Test
    public void testIsPasswordValidWithTrueCondition() {
        String password = "333333Dd";
        boolean condition = UserValidator.isPasswordValid(password);
        assertTrue(condition, "The value " + password + " is invalid");
    }

    @Test(dataProvider = "passwordData")
    public void testIsPasswordValidWithFalseCondition(String password) {
        boolean condition = UserValidator.isPasswordValid(password);
        assertFalse(condition, "The value " + password + " is valid");
    }

    @Test
    public void testIsPasswordValidWithNullCondition() {
        String password = null;
        boolean condition = UserValidator.isPasswordValid(password);
        assertFalse(condition, "The value isn't null");
    }

    @Test
    public void testIsNameValidWithTrueCondition() {
        String name = "Denis";
        boolean condition = UserValidator.isNameValid(name);
        assertTrue(condition, "The value " + name + " is invalid");
    }

    @Test(dataProvider = "nameData")
    public void testIsNameValidWithFalseCondition(String name) {
        boolean condition = UserValidator.isNameValid(name);
        assertFalse(condition, "The value " + name + " is valid");
    }

    @Test
    public void testIsNameValidWithNullCondition() {
        String name = null;
        boolean condition = UserValidator.isNameValid(name);
        assertFalse(condition, "The value isn't null");
    }

    @Test
    public void testIsEmailValidWithTrueCondition() {
        String email = "Kotov@gmail.com";
        boolean condition = UserValidator.isEmailValid(email);
        assertTrue(condition, "The value " + email + " is invalid");
    }

    @Test(dataProvider = "emailData")
    public void testIsEmailValidWithFalseCondition(String email) {
        boolean condition = UserValidator.isEmailValid(email);
        assertFalse(condition, "The value " + email + " is valid");
    }

    @Test
    public void testIsEmailValidWithNullCondition() {
        String email = null;
        boolean condition = UserValidator.isEmailValid(email);
        assertTrue(condition, "The value " + email + " is invalid");
    }

    @Test
    public void testIsMobileNumberValidWithTrueCondition() {
        String email = "447181933";
        boolean condition = UserValidator.isEmailValid(email);
        assertTrue(condition, "The value " + email + " is invalid");
    }

    @Test(dataProvider = "mobileNumberData")
    public void testIsMobileNumberValidWithFalseCondition(String email) {
        boolean condition = UserValidator.isEmailValid(email);
        assertFalse(condition, "The value " + email + " is valid");
    }

    @Test
    public void testIsMobileNumberValidWithNullCondition() {
        String email = null;
        boolean condition = UserValidator.isEmailValid(email);
        assertFalse(condition, "The value isn't null");
    }
}