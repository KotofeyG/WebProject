package com.kotov.restaurant.util.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class MealValidatorTest {
    private static final String TITLE = "title";
    private static final String TYPE = "type";
    private static final String PRICE = "price";
    private static final String RECIPE = "recipe";
    private static final String TITLE_VALUE = "Суши на понедельник";
    private static final String TYPE_VALUE = "roll";
    private static final String PRICE_VALUE = "10.55";
    private static final String RECIPE_VALUE = "лосось, сливочный сыр";

    @DataProvider(name = "mealData")
    public Object[][] createInvalidMealData() {
        Object[][] data = new Object[32][2];
        String[] titleValues = {"", "  ", " Среда", " Среда", "-Среда", "Среда-", "Среда,", ",Среда", "Ср$да",
                "abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi", "a", "Среда  и пятница"
                , "Среда--и пятница", "", "-Среда,, пятница"};
        String[] recipeValues = {" сливочный сыр", "сливочный сыр ", "-сливочный сыр", "сливочный сыр-", "сливочный сыр"
                , "слив<о>чный сыр", "сливочный сыр!", "", " ", "сливочный сыр,", "сливочный сыр,, лосось", ",сливочный сыр"
                , "сливочный сыр ", "слив#чный сыр", "слив&чный сыр"};
        String type = "rolls";
        int counter = 0;
        for (int i = 0; i < titleValues.length; i++, counter++) {
            Map<String, String> dataCheckResult = new HashMap<>();
            dataCheckResult.put(TITLE, titleValues[i]);
            dataCheckResult.put(TYPE, TYPE_VALUE);
            dataCheckResult.put(PRICE, PRICE_VALUE);
            dataCheckResult.put(RECIPE, RECIPE_VALUE);
            InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
            data [counter][0] = dataCheckResult;
            data [counter][1] = imageStream;
        }
        for (int i = 0; i < recipeValues.length; i++, counter++) {
            Map<String, String> dataCheckResult = new HashMap<>();
            dataCheckResult.put(TITLE, TITLE_VALUE);
            dataCheckResult.put(TYPE, TITLE_VALUE);
            dataCheckResult.put(PRICE, PRICE_VALUE);
            dataCheckResult.put(RECIPE, recipeValues[i]);
            InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
            data [counter][0] = dataCheckResult;
            data [counter][1] = imageStream;
        }
        Map<String, String> dataCheckResultForType = new HashMap<>();
        dataCheckResultForType.put(TITLE, TITLE_VALUE);
        dataCheckResultForType.put(TYPE, type);
        dataCheckResultForType.put(PRICE, PRICE_VALUE);
        dataCheckResultForType.put(RECIPE, RECIPE_VALUE);
        InputStream imageStreamForType = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
        Map<String, String> dataCheckResultForImage = new HashMap<>();
        dataCheckResultForImage.put(TITLE, TITLE_VALUE);
        dataCheckResultForImage.put(TYPE, TITLE_VALUE);
        dataCheckResultForImage.put(PRICE, PRICE_VALUE);
        dataCheckResultForImage.put(RECIPE, RECIPE_VALUE);
        InputStream imageStream = null;
        data[30][0] = dataCheckResultForType;
        data[30][1] = imageStreamForType;
        data[31][0] = dataCheckResultForImage;
        data[31][1] = imageStream;
        return data;
    }

    @DataProvider(name = "mealType")
    public Object[][] createMealType() {
        String[][] mealType = {{"roll"}, {"nigiri"}, {"sashimi"}, {"soup"}, {"main_dish"}, {"salad"}, {"appetizer"}};
        return mealType;
    }

    @DataProvider(name = "mealArray")
    public Object[][] createMealArray() {
        String[] mealIdArray1 = {"-1", "10", "100"};
        String[] mealNumberArray1 = {"1", "10", "100"};
        String[] mealIdArray2 = {"d", "10", "100"};
        String[] mealNumberArray2 = {"1", "10", "100"};
        String[] mealIdArray3 = {"1", "<10>", "100"};
        String[] mealNumberArray3 = {"1", "10", "100"};
        String[] mealIdArray4 = {"1", "10"};
        String[] mealNumberArray4 = {"1", "10", "100"};
        String[] mealIdArray5 = {"1", "10", ""};
        String[] mealNumberArray5 = {"1", "10", ""};
        Object[][] data = {{mealIdArray1, mealNumberArray1}, {mealIdArray2, mealNumberArray2}, {mealIdArray3, mealNumberArray3}
                , {mealIdArray4, mealNumberArray4}, {mealIdArray5, mealNumberArray5}};
        return data;
    }

    @Test
    public void testAreMealParamsValidWithTrueCondition() {
        Map<String, String> dataCheckResult = new HashMap<>();
        dataCheckResult.put(TITLE, TITLE_VALUE);
        dataCheckResult.put(TYPE, TYPE_VALUE);
        dataCheckResult.put(PRICE, PRICE_VALUE);
        dataCheckResult.put(RECIPE, RECIPE_VALUE);
        InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
        boolean condition = MealValidator.areMealParamsValid(dataCheckResult, imageStream);
        assertTrue(condition, "Parameters are invalid: " + dataCheckResult);
    }

    @Test(dataProvider = "mealData")
    public void testAreMealParamsValidWithFalseCondition(Map<String, String> dataCheckResult, InputStream imageStream) {
        boolean condition = MealValidator.areMealParamsValid(dataCheckResult, imageStream);
        assertFalse(condition, "Parameters are valid: " + dataCheckResult);
    }

    @Test
    public void testAreMealParamsValidWithNullCondition() {
        Map<String, String> dataCheckResult = null;
        InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
        boolean condition = MealValidator.areMealParamsValid(dataCheckResult, imageStream);
        assertFalse(condition, "Parameters are valid: " + dataCheckResult);
    }

    @Test(dataProvider = "mealType")
    public void testIsMealTypeValidWithTrueCondition(String type) {
        boolean condition = MealValidator.isMealTypeValid(type);
        assertTrue(condition, "Meal type " + type + " doesn't exist in this application");
    }

    @Test
    public void testIsMealTypeValidWithFalseCondition() {
        String type = "rolls1";
        boolean condition = MealValidator.isMealTypeValid(type);
        assertFalse(condition, "Meal type " + type + " exists in this application");
    }

    @Test
    public void testIsMealTypeValidWithNullCondition() {
        String type = null;
        boolean condition = MealValidator.isMealTypeValid(type);
        assertFalse(condition, "Parameter isn't null");
    }

    @Test
    public void testIsOrderExistWithTrueCondition() {
        String[] mealIdArray = {"1", "10", "100"};
        String[] mealNumberArray = {"1", "10", "100"};
        boolean condition = MealValidator.isMealInOrderExist(mealIdArray, mealNumberArray);
        assertTrue(condition, "Arrays don't contain parsable values: " + Arrays.toString(mealIdArray)
                + " and " + Arrays.toString(mealNumberArray));
    }

    @Test(dataProvider = "mealArray")
    public void testIsOrderExistWithFalseCondition(String[] mealIdArray, String[] mealNumberArray) {
        boolean condition = MealValidator.isMealInOrderExist(mealIdArray, mealNumberArray);
        assertFalse(condition, "Arrays don't contain parsable values: " + Arrays.toString(mealIdArray)
                + " and " + Arrays.toString(mealNumberArray));
    }

    @Test
    public void testIsOrderExistWithNullCondition() {
        String[] mealIdArray = null;
        String[] mealNumberArray = null;
        boolean condition = MealValidator.isMealInOrderExist(mealIdArray, mealNumberArray);
        assertFalse(condition, "Arrays isn't null");
    }
}