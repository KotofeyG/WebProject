package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.util.List;

public interface MealDao {
    boolean isMealExist(String title) throws DaoException;

    boolean isMenuExist(String title) throws DaoException;

    void addNewMeal(Meal meal) throws DaoException;

    long addNewMenu(Menu menu) throws DaoException;

    void addMealsToMenu(long menuId, List<Long> mealIdList) throws DaoException;

    void updateMealStatuses(boolean status, List<Long> mealIdList) throws DaoException;

    void removeMeals(List<Long> mealIdList) throws DaoException;

    void addMealToMenu(long menuId, long mealId) throws DaoException;

    void deleteMealFromMenu(long menuId, long mealId) throws DaoException;

    Meal getMealById(long id) throws DaoException;

    Menu findMenuById(long menuId) throws DaoException;

    List<Meal> findAllMeals() throws DaoException;

    List<Meal> findAllMealsForMenu(long menuId) throws DaoException;

    List<Menu> findAllMenus() throws DaoException;
}