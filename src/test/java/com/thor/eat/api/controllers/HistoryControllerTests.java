/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.History;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.RecordType;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.services.HistoryService;
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
 * This is the unit tests for class history apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class HistoryControllerTests extends BaseControllerTests<History> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private HistoryService historyService;

    /**
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        History obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/histories/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        History fetchedObj = objectMapper.readValue(json, History.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getOperation(), fetchedObj.getOperation());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        History obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/histories")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        History createdObj = objectMapper.readValue(createdJson, History.class);

        assertNotNull("id should be generated.", createdObj.getId());

        // verify they are equal
        assertEquals("operation should be equal", obj.getOperation(), createdObj.getOperation());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        History obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/histories")
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
        History obj = new History();
        obj.setOperation(OperationType.Delete);

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/histories/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        History updatedObject = objectMapper.readValue(updatedJson, History.class);

        // verify that it is updated
        assertEquals("should be updated", obj.getOperation(), updatedObject.getOperation());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        History obj = new History();
        obj.setOperation(OperationType.Delete);

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/histories/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/histories/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());

        // verify the object has been deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/histories/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/histories/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/histories")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<History> result = readSearchResult(json, History.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "userId";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/histories?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<History> result = readSearchResult(json, History.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected History generateTestObject() throws Exception {
        History history = new History();
        history.setOperation(OperationType.Insert);
        history.setRecordType(RecordType.Standard);
        history.setModifiedDate(new Date());
        history.setRecordId(1L);
        history.setUser(getUser());
        return history;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected History createTestObject(History obj) throws Exception {
        return historyService.create(obj);
    }

}
