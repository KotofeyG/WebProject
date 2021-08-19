package com.kotov.restaurant.model.service;

import com.kotov.restaurant.exception.ServiceException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuService {

    List<Meal> findAllMeals() throws ServiceException;

    List<Menu> findAllMenu() throws ServiceException;

    List<Meal> findMealsByType(String mealType) throws ServiceException;

    Optional<Menu> findMenuById(String menuIdStr) throws ServiceException;

    List<Meal> findMealsForMenuByPresence(long menuId, int page) throws ServiceException;

    int getMealCountForMenu(long menuId) throws ServiceException;

    boolean addMealToUserCart(long userId, String mealIdStr, String mealQuantityStr) throws ServiceException;

    boolean insertNewMeal(Map<String, String> dataCheckResult, InputStream image) throws ServiceException;

    boolean insertNewMenu(String title, String type, String[] mealIdArray) throws ServiceException;

    boolean addMealToMenu(String menuIdStr, String mealIdStr) throws ServiceException;

    boolean updateMealStatusesById(boolean status, String[] mealIdArray) throws ServiceException;

    boolean deleteMealsById(String[] mealIdArray) throws ServiceException;

    boolean deleteMenuById(String menuIdStr) throws ServiceException;

    boolean deleteMealFromMenu(String menuIdStr, String mealIdStr) throws ServiceException;
}