/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
@Table(name = "ChangeRequest")
public class ChangeRequest extends IdentifiableEntity {
	@ManyToOne
    @JoinColumn(name = "requestedByUserId")
    private User requestedUser;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;

    private OperationType type;

    @OneToOne
    @JoinColumn(name = "pendingStandardId")
    private PendingStandard pendingStandard;

    private Long standardId;

    @NotNull(groups = ValidationGroup.Create.class)
    @Enumerated(EnumType.STRING)
    private RecordType recordType;
}
