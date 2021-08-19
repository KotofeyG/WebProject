package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;

import java.io.InputStream;
import java.util.List;

public interface MealDao extends BaseDao<Meal>{
    boolean isMealExist(String title) throws DaoException;

    List<Meal> findMealsByType(Meal.Type type) throws DaoException;

    long insertNewEntity(Meal meal, InputStream image) throws DaoException;

    boolean insertMealToUserCart(long userId, long mealId, int mealQuantity) throws DaoException;

    boolean updateMealStatusesById(boolean status, List<Long> mealIdList) throws DaoException;
}