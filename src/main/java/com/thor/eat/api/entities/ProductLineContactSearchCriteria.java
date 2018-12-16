/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The product line contact search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class ProductLineContactSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the product line id to search.
     */
    private Long productLineId;
}

