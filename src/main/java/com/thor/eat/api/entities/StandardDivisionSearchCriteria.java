/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.List;


/**
 * The standard division search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class StandardDivisionSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the division id to search for.
     */
    @SafeHtml
    private String divisionId;

    /**
     * Represents the sub division id to search for.
     */
    @SafeHtml
    private String subDivisionId;

    /**
     * Represents the product line id.
     */
    @SafeHtml
    private String productLine;

    /**
     * Represents the organization id.
     */
    private Long organizationId;

    /**
     * Represents the standard id to search.
     */
    private Long standardId;

    /**
     * Represents the email to search.
     */
    @Email
    private String email;

    /**
     * Represents the keyword.
     */
    @SafeHtml
    private String keyword;

    /**
     * Represents standard description
     */
    @SafeHtml
    private String description;

    private List<Long> ids;

    private Boolean isApproved;

    @SafeHtml
    private String standardName;
}

