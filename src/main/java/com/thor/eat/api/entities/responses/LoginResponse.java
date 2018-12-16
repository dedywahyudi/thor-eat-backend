/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.entities.responses;


import com.thor.eat.api.entities.IdentifiableEntity;
import com.thor.eat.api.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * This class is the base class for login response.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class LoginResponse extends IdentifiableEntity {

    /**
     * Represents the username of the user.
     */
    private String username;
    /**
     * Represents the email of the user.
     */
    private String email;
    private String fullName;
    private Role role;
    private String accessToken;
    private Date accessTokenExpiresDate;
}
