/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services;


import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.ThorEarlyException;

import java.util.List;

/**
 * The generic service. Served as a base interface for search operations.
 * @param <T> the entity
 * @param <S> the search criteria
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface SearchGenericService<T, S> {

    /**
     * This method is used to search for results by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws ThorEarlyException if any other error occurred during operation
     */
    SearchResult<T> search(S criteria, Paging paging) throws ThorEarlyException;

    /**
     * This method is used to search for results by criteria.
     *
     * @param criteria the search criteria
     *
     * @return the search result
     *
     * @throws ThorEarlyException if any other error occurred during operation
     */
    List<T> search(S criteria) throws ThorEarlyException;

}

