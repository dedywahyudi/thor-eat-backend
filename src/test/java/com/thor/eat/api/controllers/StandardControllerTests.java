/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.*;
import com.thor.eat.api.services.StandardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class standard apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardControllerTests extends BaseControllerTests<Standard> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private StandardService standardService;

    /**
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        Standard obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standards/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Standard fetchedObj = objectMapper.readValue(json, Standard.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getDescription(), fetchedObj.getDescription());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        Standard obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/standards")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        ChangeRequest changeRequest = objectMapper.readValue(createdJson, ChangeRequest.class);

        assertNotNull("id should be generated.", changeRequest.getId());

        // verify they are equal
        assertEquals("operation should be equal", OperationType.Insert, changeRequest.getType());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        Standard obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/standards")
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
        long id = standardDivision.getId();
        Standard obj = new Standard();
        obj.setDescription("updated-description");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/standards/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ChangeRequest changeRequest = objectMapper.readValue(updatedJson, ChangeRequest.class);

        // verify that it is updated
        assertNotNull("id should be generated.", changeRequest.getId());

        // verify they are equal
        assertEquals("operation should be equal", OperationType.Update, changeRequest.getType());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        Standard obj = new Standard();
        obj.setDescription("updated-description");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/standards/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the delete api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void delete() throws Exception {
        long id = standardDivision.getId();

        String json = mockAuthMvc.perform(MockMvcRequestBuilders.delete("/standards/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ChangeRequest changeRequest = objectMapper.readValue(json, ChangeRequest.class);

        // verify that it is updated
        assertNotNull("id should be generated.", changeRequest.getId());

        // verify they are equal
        assertEquals("operation should be equal", OperationType.Delete, changeRequest.getType());

    }

    /**
     * Tests the delete api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void deleteAuthorization() throws Exception {
        long id = getTestObject().getId();

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/standards/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standards")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Standard> result = readSearchResult(json, Standard.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "description";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/standards?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Standard> result = readSearchResult(json, Standard.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Standard generateTestObject() throws Exception {
        Standard standard = new Standard();
        standard.setName("standard");
        standard.setOrganization(organization);
        standard.setCreatedDate(new Date());
        standard.setCreatedBy(getUser().getId());
        standard.setEdition("test-edition");
        standard.setDate(new Date());
        return standard;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Standard createTestObject(Standard obj) throws Exception {
        return standardService.create(obj);
    }

}
