package com.kotov.restaurant.model.dao;

import com.kotov.restaurant.exception.DaoException;
import com.kotov.restaurant.model.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author Denis Kotov
 *
 * The {@link BaseDao} interface
 * is a base interface for other DAO interfaces,
 * provides access to the database
 */
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

    /**
     * @param id sought entity id
     * @return Optional sought entity
     * @throws DaoException if the request to data base could not be handled
     */
    Optional<T> findEntityById(long id) throws DaoException;

    /**
     * @return collection of all entities
     * @throws DaoException if the request to data base could not be handled
     */
    List<T> findAllEntities() throws DaoException;

    /**
     * @param entity that will be added to data base
     * @return entity id  if entity was added otherwise 0
     * @throws DaoException if the request to data base could not be handled
     */
    long insertNewEntity(T entity) throws DaoException;

    /**
     * @param id with which the entity will be deleted
     * @return true if entity was deleted otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean deleteEntityById(long id) throws DaoException;

    /**
     * @param idList with which the entities will be deleted
     * @return true if at lest one of entities were deleted otherwise false
     * @throws DaoException if the request to data base could not be handled
     */
    boolean deleteEntitiesById(List<Long> idList) throws DaoException;

    /**
     * @param query current query to data base
     * @param pageNumber current page for displaying entities
     * @return query with current page
     */
    default String buildPaginatedQuery(String query, int pageNumber) {
        int limit = PAGE_SIZE;
        int offset = (limit * pageNumber) - limit;
        StringBuilder queryBuilder = new StringBuilder(query);
        queryBuilder.append(LIMIT);
        if (offset > 0) {
            queryBuilder.append(offset).append(SEPARATOR);
        }
        queryBuilder.append(limit);
        return queryBuilder.toString();
    }
}