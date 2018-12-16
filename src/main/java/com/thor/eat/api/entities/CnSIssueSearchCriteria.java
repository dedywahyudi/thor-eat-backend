/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The cns issue search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class CnSIssueSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the region to search for.
     */
    @SafeHtml
    private String region;

    /**
     * Represents the id of the division to search for.
     */
    @SafeHtml
    private String divisionId;

    /**
     *  filter, true to return only issues created by the current logged-in user
     */
    private Boolean createdByMe;


    /**
     * Represents the keyword filter, search in description, actionPlan,
     * financialImpact, impactSummary, successionPlan, or roadmapAlignment containing the keyword (case insensitive)
     */
    @SafeHtml
    private String keyword;

    /**
     *  filter, using priority
     */
    private Integer priority;
}

