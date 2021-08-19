package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    int PAGE_SIZE = 4;
    String LIMIT = " LIMIT ";
    String SEPARATOR = ", ";
    int FIRST_PARAM_INDEX = 1;
    int SECOND_PARAM_INDEX = 2;
    int THIRD_PARAM_INDEX = 3;
    int FOURTH_PARAM_INDEX = 4;
    int FIFTH_PARAM_INDEX = 5;
    int SIXTH_PARAM_INDEX = 6;
    int SEVENTH_PARAM_INDEX = 7;
    int EIGHT_PARAM_INDEX = 8;
    int NINE_PARAM_INDEX = 9;

    Optional<T> findEntityById(long id) throws DaoException;

    List<T> findAllEntities() throws DaoException;

    long insertNewEntity(T entity) throws DaoException;

    boolean deleteEntityById(long id) throws DaoException;

    boolean deleteEntitiesById(List<Long> idList) throws DaoException;

    default String buildPaginatedQuery(String mainQuery, int pageNumber) {
        int limit = PAGE_SIZE;
        int offset = (limit * pageNumber) - limit;
        StringBuilder queryBuilder = new StringBuilder(mainQuery);
        queryBuilder.append(LIMIT);
        if (offset > 0) {
            queryBuilder.append(offset).append(SEPARATOR);
        }
        queryBuilder.append(limit);
        return queryBuilder.toString();
    }
}