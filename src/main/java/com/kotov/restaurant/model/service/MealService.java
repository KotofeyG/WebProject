package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.util.List;
import java.util.Map;

public interface MealService {
    boolean addNewMeal(Map<String, String> dataCheckResult) throws ServiceException;

    boolean addNewMenu(String title, String type, String[] mealIdArray) throws ServiceException;

    void updateMealStatuses(boolean status, String[] mealIdArray) throws ServiceException;

    void removeMeals(String[] mealIdArray) throws ServiceException;

    void addMealToMenu(String menuIdStr, String mealIdStr) throws ServiceException;

    void deleteMealFromMenu(String menuIdStr, String mealIdStr) throws ServiceException;

    List<Meal> findAllMeals() throws ServiceException;

    List<Menu> findAllMenu() throws ServiceException;

    Menu findMenuById(String menuIdStr) throws ServiceException;
}