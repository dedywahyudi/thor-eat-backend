/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.responses.LoginResponse;
import com.thor.eat.api.services.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * This is the base class for all the controller tests.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public abstract class BaseControllerTests<T> extends BaseTests<T> {

    /**
     * The web application context.
     */
    @Autowired
    protected WebApplicationContext context;

    /**
     * The object mapper.
     */
    protected ObjectMapper objectMapper = buildObjectMapper();


    /**
     * The mock mvc object.
     */
    protected MockMvc mockMvc;

    /**
     * The mock mvc object with security support.
     */
    protected MockMvc mockAuthMvc;

    /**
     * The spring security filter chain.
     */
    @Autowired
    private Filter springSecurityFilterChain;

    /**
     * Represents the user service.
     */
    @Autowired
    private UserService userService;

    /**
     * Represents the bearer token.
     */
    protected String bearerToken;

    /**
     * Setup test.
     *
     * @throws Exception throws if any error happen
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.mockMvc = webAppContextSetup(context).build();
        this.mockAuthMvc = webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();

        LoginResponse response = userService.login("admin", "Password123");
        bearerToken = response.getAccessToken();
    }


    /**
     * Read search result from json.
     *
     * @param json the json.
     * @param target the target class
     * @param <T> the class name
     * @return the search result.
     * @throws Exception throws if any error happen
     */
    protected <T> SearchResult<T> readSearchResult(String json, Class<T> target) throws Exception {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(
                SearchResult.class, target);
        return objectMapper.readValue(json, type);
    }

    /**
     * Build object mapper with custom date format.
     * @return the custom object mapper.
     */
    public static ObjectMapper buildObjectMapper(){
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        return builder.build();
    }

}
