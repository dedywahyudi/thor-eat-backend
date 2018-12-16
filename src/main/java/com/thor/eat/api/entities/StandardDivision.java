/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thor.eat.api.utils.StringListConverter;
import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for organization.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "StandardDivision")
public class StandardDivision extends IdentifiableEntity {
    @NotNull(groups = ValidationGroup.Create.class)
    private Boolean criticalToBusiness;

    private String comment;

    @Convert(converter = StringListConverter.class)
    private List<String> standardParticipant = new ArrayList<>();

    @JsonIgnore
    @Column(name = "standardParticipant", updatable = false, insertable = false)
    private String rawStandardParticipant;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "standardId", nullable = true)
    private Standard standard;

    @NotNull(groups = ValidationGroup.Create.class)
    @ManyToOne
    @JoinColumn(name = "divisionId", referencedColumnName = "id")
    private Division division;

    @ManyToOne
    @JoinColumn(name = "subDivisionId")
    private Division subDivision;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productLineId")
    private ProductLine productLine;

//    @OneToMany(mappedBy = "standardDivision", cascade = CascadeType.ALL)
//    private List<StandardDivisionContact> standardDivisionContact = new ArrayList<>();

    private Boolean isApproved;

//    public void addContact(StandardDivisionContact contact) {
//    	contact.setStandardDivision(this);
//    	standardDivisionContact.add(contact);
//    }
//    public void removeContact(StandardDivisionContact contact) {
//    	standardDivisionContact.remove(contact);
//    	contact.setStandardDivision(null);
//    }
}
