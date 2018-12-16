/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The standard search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class StandardSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the organization id to search for.
     */
    private Long organizationId;

    /**
     * Represents the keyword.
     */
    @SafeHtml
    private String keyword;
}

