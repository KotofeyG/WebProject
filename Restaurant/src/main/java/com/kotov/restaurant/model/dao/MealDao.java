package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.User;

import java.io.InputStream;
import java.util.List;

public interface MealDao extends BaseDao<Meal>{

    /**
     * @param title current title
     * @return true if meal exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isMealExist(String title) throws DaoException;

    /**
     * @param mealId current {@link Meal} id
     * @return true if {@link Meal} available, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isMealAvailable(long mealId) throws DaoException;

    /**
     * @param type The type by which the dishes will be found
     * @return collection of {@link Meal}s
     * @throws DaoException if the request to data base could not be handled
     */
    List<Meal> findMealsByType(Meal.Type type) throws DaoException;

    /**
     * @param meal current {@link Meal}
     * @param image {@link Meal}s image
     * @return get user id if {@link Meal} was added to data base, otherwise 0
     * @throws DaoException if the request to data base could not be handled
     */
    long insertNewEntity(Meal meal, InputStream image) throws DaoException;

    /**
     * @param userId {@link User}s id for which will be added meals into his cart
     * @param mealId current {@link Meal} id
     * @param mealQuantity the number of {@link Meal}s
     * @return true if {@link Meal}s were added into {@link User} cart
     * @throws DaoException if the request to data base could not be handled
     */
    boolean insertMealToUserCart(long userId, long mealId, int mealQuantity) throws DaoException;

    /**
     * @param mealIdList {@link Meal}s id, which  status should be changed
     * @param status current status
     * @return true if status was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateMealStatusesById(boolean status, List<Long> mealIdList) throws DaoException;
}