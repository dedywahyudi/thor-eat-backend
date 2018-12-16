/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.requests.LoginRequest;
import com.thor.eat.api.entities.requests.StandardDivisionsEmailRequest;
import com.thor.eat.api.entities.responses.LoginResponse;
import com.thor.eat.api.repositories.StandardDivisionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class changeRequest apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class HomeControllerTests extends BaseControllerTests<StandardDivision> {

    /**
     * The environment variables
     */
    @Autowired
    private Environment env;


    /**
     * Represents the standard division repository.
     */
    @Autowired
    private StandardDivisionRepository standardDivisionRepository;

    /**
     * Represents the send to email address.
     */
    @Value("${email.sendTo}")
    private String sendToEmail;

    /**
     * Tests the login api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void login() throws Exception {
        LoginRequest obj = new LoginRequest();
        obj.setUsername(getUser().getUsername());
        obj.setPassword(getUser().getPassword());

        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/login")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        LoginResponse response = objectMapper.readValue(createdJson, LoginResponse.class);
        assertNotNull("should return a access token", response.getAccessToken());
    }

    /**
     * Tests the login api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void loginFailed() throws Exception {
        LoginRequest obj = new LoginRequest();
        obj.setUsername(getUser().getUsername());
        obj.setPassword(this.env.getProperty("password.wrong"));

        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/login")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * To send email.
     * @throws Exception if there are any errors.
     */
    @Test
    public void sendEmail() throws Exception {
        StandardDivisionsEmailRequest request = new StandardDivisionsEmailRequest();
        request.setBody("test body");
        request.setTo(Arrays.asList(sendToEmail, sendToEmail));
        request.setSubject("test subject");
        request.setStandardDivisionIds(Arrays.asList(getTestObject().getId(), getTestObject().getId()));
        String json = objectMapper.writeValueAsString(request);
        mockAuthMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected StandardDivision generateTestObject() throws Exception {
        StandardDivision standardDivision = new StandardDivision();
        standardDivision.setComment("this is a comment");
        standardDivision.setCriticalToBusiness(true);
        standardDivision.setDivision(division);
        standardDivision.setStandard(standard);
        standardDivision.setSubDivision(subDivision);
        standardDivision.setIsApproved(false);
        List<String> participants = new ArrayList<>();
        participants.add("test@test.com");
        standardDivision.setStandardParticipant(participants);
        return standardDivision;
    }

    @Override
    protected StandardDivision createTestObject(StandardDivision obj) throws Exception {
        return standardDivisionRepository.save(obj);
    }
}
