/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The division search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class DivisionSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the region to search.
     */
    @SafeHtml
    private String region;

    private String keyword;
}

