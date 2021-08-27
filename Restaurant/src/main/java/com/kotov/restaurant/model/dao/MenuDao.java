package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.Meal;
import com.kotov.restaurant.model.entity.Menu;

import java.util.List;

public interface MenuDao extends BaseDao<Menu>{

    /**
     * @param title current title
     * @return true if {@link Menu} exists, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean isMenuExist(String title) throws DaoException;

    /**
     * @param menuId {@link Menu}s id for which will be find {@link Meal}s
     * @return collection of {@link Address}es
     * @throws DaoException if the request to data base could not be handled
     */
    List<Meal> findMealsForMenu(long menuId) throws DaoException;

    /**
     * @param menuId {@link Menu}s id for which will be find available {@link Meal}s
     * @param page current page
     * @return collection of {@link Meal}es
     * @throws DaoException if the request to data base could not be handled
     */
    List<Meal> findAvailableMealsForMenu(long menuId, int page) throws DaoException;

    /**
     * @param menuId {@link Menu} according that will be counted rows
     * @return count of rows
     * @throws DaoException if the request to data base could not be handled
     */
    int getAvailableMealCountForMenu(long menuId) throws DaoException;

    /**
     * @param menu current {@link Menu}
     * @param mealIdList {@link Meal} list which will be added to {@link Menu}
     * @return get user id if {@link Meal} was added to data base, otherwise 0
     * @throws DaoException if the request to data base could not be handled
     */
    long insertNewEntity(Menu menu, List<Long> mealIdList) throws DaoException;

    /**
     * @param menuId {@link Menu}s id for which will be added new {@link Meal}
     * @param mealId {@link Meal} id
     * @return true if {@link Meal} was added to {@link Menu}, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean insertMealToMenu(long menuId, long mealId) throws DaoException;

    /**
     * @param menuId {@link Menu}s id, for which title should be changed
     * @param title new title
     * @return true if title was changed, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean updateMenuTitle(long menuId, String title) throws DaoException;

    /**
     * @param menuId {@link Menu}s id, for which {@link Meal} should be deleted from cart
     * @param mealId {@link Meal}s id
     * @return true if {@link Meal} was deleted, otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean deleteMealFromMenu(long menuId, long mealId) throws DaoException;
}