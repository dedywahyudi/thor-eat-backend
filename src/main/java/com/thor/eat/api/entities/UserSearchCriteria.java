/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.List;


/**
 * The user search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class UserSearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the keyword to search.
     */
    @SafeHtml
    private String keyword;

    private List<RoleType> roleTypes;
}

