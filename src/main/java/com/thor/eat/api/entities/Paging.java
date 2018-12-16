/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The paging parameters.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class Paging {
    /**
     * The page number.
     */
    private int offset;

    /**
     * The page size.
     */
    private int limit;

    /**
     * The sort column.
     */
    @SafeHtml
    private String sortColumn;

    /**
     * The sort order.
     */
    private SortDirection sortDirection;

}

