/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This class is for organization.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "Division")
public class Division {
    @NotNull(groups = ValidationGroup.Create.class)
    @Id
    private String id;

    @NotNull(groups = ValidationGroup.Create.class)
    @SafeHtml
    private String name;

    @NotNull(groups = ValidationGroup.Create.class)
    @SafeHtml
    private String region;

    @ManyToOne
    @JoinColumn(name = "parentDivisionId")
    private Division parentDivision;
}
