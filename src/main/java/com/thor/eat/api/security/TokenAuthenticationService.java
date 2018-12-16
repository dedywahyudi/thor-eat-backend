/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.security;


import com.thor.eat.api.entities.User;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.UserRepository;
import com.thor.eat.api.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * The token auth service.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Service
public class TokenAuthenticationService {
    /**
     * The token auth header fullName.
     */
    private static final String AUTH_HEADER_NAME = "Authorization";

    /**
     *  Represents the separator in the authorization header.
     */
    private static final String AUTHORIZATION_SEPARATOR = " ";

    /**
     * Represents the repository for accessing the bearer token.
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * The token auth service constructor.
     */
    public TokenAuthenticationService() {

    }

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userRepository, "userRepository");
    }


    /**
     * Get user auth object from request.
     *
     * @param request the servlet request.
     * @return the user auth request.
     */
    public UserAuthentication getAuthentication(HttpServletRequest request) {
        final String tokenString = request.getHeader(AUTH_HEADER_NAME);
        if (tokenString != null && tokenString.split(AUTHORIZATION_SEPARATOR).length > 1) {
            final String token = tokenString.split(AUTHORIZATION_SEPARATOR)[1];
            User user;
            try {
                user = getUserByAccessToken(token);
            } catch (ThorEarlyException e) {
                user = null;
            }
            if (user != null) {
                UserAuthentication authentication = new UserAuthentication(user);
                return authentication;
            }
        }
        return null;
    }

    /**
     * Gets the user entity from the bearer token.
     * @param token the bearer token.
     * @return the user entity or null if not exists.
     * @throws ThorEarlyException if there are any errors.
     */
    private User getUserByAccessToken(String token) throws ThorEarlyException {
        List<User> result = userRepository.findByAccessToken(token);
        if (result.size() == 0) {
            // cannot find users of that token
            return null;
        }
        // validate the token
        User user = result.get(0);
        if (user.getAccessTokenExpiresDate().getTime() < new Date().getTime()) {
            // the token has been expired
            return null;
        }
        return user;
    }
}
