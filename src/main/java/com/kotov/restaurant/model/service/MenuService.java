package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuService {

    boolean addNewMeal(Map<String, String> dataCheckResult, InputStream image) throws ServiceException;

    boolean addNewMenu(String title, String type, String[] mealIdArray) throws ServiceException;

    boolean updateMealStatusesById(boolean status, String[] mealIdArray) throws ServiceException;

    List<Meal> findMealsForMenu(long menuId) throws ServiceException;

    boolean removeMealsById(String[] mealIdArray) throws ServiceException;

    void addMealToMenu(String menuIdStr, String mealIdStr) throws ServiceException;

    void deleteMealFromMenu(String menuIdStr, String mealIdStr) throws ServiceException;

    List<Meal> findMealsByType(String mealType) throws ServiceException;

    List<Meal> findAllMeals() throws ServiceException;

//    Map<Meal, String> findAllMeals1() throws ServiceException;

    List<Menu> findAllMenu() throws ServiceException;

    Optional<Menu> findMenuById(String menuIdStr) throws ServiceException;

    boolean deleteMenuById(String menuIdStr) throws ServiceException;


}