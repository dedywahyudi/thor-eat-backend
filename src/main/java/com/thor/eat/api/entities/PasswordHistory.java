/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * This class is the base class for admin and customer.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "PasswordHistory")
public class PasswordHistory extends IdentifiableEntity {
    /**
     * Represents the user id of the password belongs.
     */
    private long userId;

    /**
     * The password (hashed).
     */
    private String password;

    /**
     * Represents the date that creates this password.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}
