/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * The search result.
 * @param <T> the entity type
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class SearchResult<T> {
    /**
     * The total number.
     */
    private long total;

    /**
     * Represents the query of the pagination.
     */
    private Map<String, Object> query;

    /**
     * The values.
     */
    private List<T> items;
}

