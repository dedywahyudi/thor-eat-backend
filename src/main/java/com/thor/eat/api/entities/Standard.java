/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * This class is for organization.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "Standard")
public class Standard extends IdentifiableEntity {
    @NotBlank(groups = ValidationGroup.Create.class)
    @SafeHtml
    private String name;

    @SafeHtml
    private String description;

    @SafeHtml
    private String edition;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @NotNull(groups = ValidationGroup.Create.class)
    @ManyToOne
    @JoinColumn(name = "organizationId")
    private Organization organization;

}
