/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
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
@Table(name = "History")
public class History extends IdentifiableEntity {
    @NotNull(groups = ValidationGroup.Create.class)
    @Min(1)
    private Long recordId;

    @SafeHtml
    private String recordName;

    @NotNull(groups = ValidationGroup.Create.class)
    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    @NotNull(groups = ValidationGroup.Create.class)
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @NotNull(groups = ValidationGroup.Create.class)
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
}
