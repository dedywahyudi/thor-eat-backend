/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The product line search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class ProductLineSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the division id to search.
     */
    @SafeHtml
    private String divisionId;

    /**
     * Represents the subDivisionId to search
     */
    private Long subDivisionId;
}

