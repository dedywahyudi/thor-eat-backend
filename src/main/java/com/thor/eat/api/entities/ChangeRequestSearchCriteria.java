/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


/**
 * The change request search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class ChangeRequestSearchCriteria extends BaseSearchCriteria {
    @SafeHtml
    private String keyword;
}

