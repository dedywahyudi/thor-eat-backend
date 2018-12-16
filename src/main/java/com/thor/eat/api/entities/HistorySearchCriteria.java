/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The history search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class HistorySearchCriteria extends BaseSearchCriteria {
    /**
     * Represents the user id to search for.
     */
    private Long userId;
}

