/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.requests.LoginRequest;
import com.thor.eat.api.entities.requests.StandardDivisionsEmailRequest;
import com.thor.eat.api.entities.responses.LoginResponse;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.EmailService;
import com.thor.eat.api.services.UserService;
import com.thor.eat.api.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.groups.Default;

/**
 * The user REST controller. Is effectively thread safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RestController
@RequestMapping("/")
@NoArgsConstructor
public class HomeController {

    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "userService");
        Helper.checkConfigNotNull(emailService, "emailService");
    }


    /**
     * This method is used to login.
     *
     * @param request the login request.
     * @return the login response.
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public LoginResponse login(@Validated(Default.class) @RequestBody LoginRequest request)
            throws ThorEarlyException  {
        return userService.login(request.getUsername(), request.getPassword());
    }

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void sendEmail(@Validated @RequestBody StandardDivisionsEmailRequest request) throws ThorEarlyException {
        emailService.sendStandardDivisionsEmail(request);
    }
}

