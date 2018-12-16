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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
@Table(name = "CnSIssue")
public class CnSIssue extends IdentifiableEntity {
    @SafeHtml
    private String description;

    @SafeHtml
    private String actionPlan;

    @SafeHtml
    private String personsResponsible;

    @SafeHtml
    private String creatorName;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date estimatedCompletionDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date engineerReviewDate;

    @SafeHtml
    private String engineerLeader;

    @SafeHtml
    private String financialImpact;

    @SafeHtml
    private String impactSummary;

    @SafeHtml
    private String vpgmName;

    private Boolean criticalRoleLeadership;

    @SafeHtml
    private String successionPlan;

    @SafeHtml
    private String offensiveDefensive;

    @SafeHtml
    private String externalPartners;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date vpgmReviewDate;

    private Integer priority;

    @SafeHtml
    private String region;

    @SafeHtml
    private String roadmapAlignment;

    @SafeHtml
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Long standardId;

    @SafeHtml
    @NotBlank(groups = ValidationGroup.Create.class)
    private String divisionId;

    @SafeHtml
    @Transient
    private String division;

    @Column(name = "[read]")
    private Boolean read;
}
