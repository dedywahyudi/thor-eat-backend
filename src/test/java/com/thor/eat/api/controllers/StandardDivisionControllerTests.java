/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.StandardDivisionSearchCriteria;
import com.thor.eat.api.services.StandardDivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class standardDivision apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardDivisionControllerTests extends BaseControllerTests<StandardDivision> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private StandardDivisionService standardDivisionService;


    /**
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        StandardDivision obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standardDivisions/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        StandardDivision fetchedObj = objectMapper.readValue(json, StandardDivision.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getComment(), fetchedObj.getComment());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        StandardDivision obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/standardDivisions")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        StandardDivision createdObj = objectMapper.readValue(createdJson, StandardDivision.class);

        assertNotNull("id should be generated.", createdObj.getId());

        // verify they are equal
        assertEquals("operation should be equal", obj.getComment(), createdObj.getComment());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        StandardDivision obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/standardDivisions")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }


    /**
     * Tests the update api.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        StandardDivision obj = new StandardDivision();
        obj.setComment("updated comment");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/standardDivisions/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        StandardDivision updatedObject = objectMapper.readValue(updatedJson, StandardDivision.class);

        // verify that it is updated
        assertEquals("should be updated", obj.getComment(), updatedObject.getComment());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        StandardDivision obj = new StandardDivision();
        obj.setComment("updated comment");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/standardDivisions/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the delete api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void delete() throws Exception {
        long id = getTestObject().getId();

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/standardDivisions/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());

        // verify the object has been deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/standardDivisions/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the delete api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void deleteAuthorization() throws Exception {
        long id = getTestObject().getId();

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/standardDivisions/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
//        List<StandardDivision> list = standardDivisionService.search(new StandardDivisionSearchCriteria());
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standardDivisions")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<StandardDivision> result = readSearchResult(json, StandardDivision.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "comment";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standardDivisions?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<StandardDivision> result = readSearchResult(json, StandardDivision.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
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

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected StandardDivision createTestObject(StandardDivision obj) throws Exception {
        return standardDivisionService.create(obj);
    }

}
