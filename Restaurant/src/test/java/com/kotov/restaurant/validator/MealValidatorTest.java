package com.kotov.restaurant.validator;

import com.oracle.wls.shaded.org.apache.regexp.RE;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    private static final String RECIPE_VALUE = "лосось, сливочный сыр, омлетный блинчик, икра лосося, рис, нори";

    @DataProvider
    public Object[][] createInvalidMealData() {
        Map<String, String>[][] data = new Map[32][1];
        String[] titleValues = {"", "  ", " Среда", " Среда", "-Среда", "Среда-", "Среда,", ",Среда", "Ср$да",
                "abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi abcdi", "a", "Среда  и пятница"
                , "Среда--и пятница", "", "-Среда,, пятница"};
        String[] recipeValues = {" сливочный сыр", "сливочный сыр ", "-сливочный сыр", "сливочный сыр-", "сливочный сыр"
                , "слив<о>чный сыр", "сливочный сыр!", "", " ", "сливочный сыр,", "сливочный сыр,, лосось", ",сливочный сыр"
                , "сливочный сыр ", "слив#чный сыр", "слив&чный сыр"};
        String type = "rolls";

        int counter = 0;
        for (int i = 0;counter < titleValues.length; i++, counter++) {
            Map<String, String> dataCheckResult = new HashMap<>();
            dataCheckResult.put(TITLE, titleValues[i]);
            dataCheckResult.put(TYPE, TYPE_VALUE);
            dataCheckResult.put(PRICE, PRICE_VALUE);
            dataCheckResult.put(RECIPE, RECIPE_VALUE);
            InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
            data [counter][0] = dataCheckResult;
        }
        for (int i = 0;counter < recipeValues.length; i++, counter++) {
            Map<String, String> dataCheckResult = new HashMap<>();
            dataCheckResult.put(TITLE, TITLE_VALUE);
            dataCheckResult.put(TYPE, TITLE_VALUE);
            dataCheckResult.put(PRICE, PRICE_VALUE);
            dataCheckResult.put(RECIPE, recipeValues[i]);
            InputStream imageStream = new ByteArrayInputStream("image".getBytes(StandardCharsets.UTF_8));
            data [counter][0] = dataCheckResult;
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
        data[31][0] = dataCheckResultForType;
        data[32][0] = dataCheckResultForImage;
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

    @Test
    public void testAreMealParamsValidWithFalseCondition() {
    }

    @Test
    public void testAreMealParamsValidWithNullCondition() {
    }

    @Test
    public void testIsMealTypeExistWithTrueCondition() {
    }

    @Test
    public void testIsMealTypeExistWithFalseCondition() {
    }

    @Test
    public void testIsMealTypeExistWithNullCondition() {
    }

    @Test
    public void testIsOrderExistWithTrueCondition() {
    }

    @Test
    public void testIsOrderExistWithFalseCondition() {
    }

    @Test
    public void testIsOrderExistWithNullCondition() {
    }
}