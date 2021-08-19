package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.util.List;

public interface MenuDao extends BaseDao<Menu>{
    boolean isMenuExist(String title) throws DaoException;

    List<Meal> findMealsForMenu(long menuId) throws DaoException;

    List<Meal> findMealsForMenuByPresence (long menuId, int page) throws DaoException;

    int getMealCountForMenu(long menuId) throws DaoException;

    long insertNewEntity(Menu menu, List<Long> mealIdList) throws DaoException;

    boolean insertMealToMenu(long menuId, long mealId) throws DaoException;

    boolean deleteMealFromMenu(long menuId, long mealId) throws DaoException;
}