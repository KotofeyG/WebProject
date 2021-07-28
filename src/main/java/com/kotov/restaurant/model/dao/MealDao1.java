package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;

import java.util.List;

public interface MealDao1 extends BaseDao<Meal>{
    boolean isMealExist(String title) throws DaoException;

    void updateMealStatuses(boolean status, List<Long> idList) throws DaoException;
}