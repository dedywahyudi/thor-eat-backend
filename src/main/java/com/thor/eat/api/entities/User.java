/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thor.eat.api.utils.ValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * This class is the base class for admin and customer.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "[User]")
public class User extends IdentifiableEntity {

    /**
     * Represents the username of the user.
     */
    @SafeHtml
    @NotNull(groups = ValidationGroup.Create.class)
    @Length(max = 200)
    private String username;

    /**
     * Represents the email of the user.
     */
    @SafeHtml
    @NotNull(groups = ValidationGroup.Create.class)
    @Email
    @Length(max = 200)
    private String email;

    @SafeHtml
    @NotNull(groups = ValidationGroup.Create.class)
    @Length(max = 200)
    private String fullName;

    /**
     * The password (hashed).
     */
    @SafeHtml
    @NotNull(groups = ValidationGroup.Create.class)
    @JsonProperty(access = WRITE_ONLY)
    @Transient
    @Length(max = 100)
    private String password;

    @NotNull(groups = ValidationGroup.Create.class)
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessTokenExpiresDate;
}
