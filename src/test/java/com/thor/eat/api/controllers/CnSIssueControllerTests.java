/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.services.CnSIssueService;
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
 * This is the unit tests for class cnSIssue apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CnSIssueControllerTests extends BaseControllerTests<CnSIssue> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private CnSIssueService cnSIssueService;

    /**
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        CnSIssue obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/cnSIssues/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CnSIssue fetchedObj = objectMapper.readValue(json, CnSIssue.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getDescription(), fetchedObj.getDescription());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        CnSIssue obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/cnSIssues")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        CnSIssue createdObj = objectMapper.readValue(createdJson, CnSIssue.class);

        assertNotNull("id should be generated.", createdObj.getId());

        // verify they are equal
        assertEquals("operation should be equal", obj.getDescription(), createdObj.getDescription());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        CnSIssue obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/cnSIssues")
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
        CnSIssue obj = new CnSIssue();
        obj.setDescription("description");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/cnSIssues/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CnSIssue updatedObject = objectMapper.readValue(updatedJson, CnSIssue.class);

        // verify that it is updated
        assertEquals("should be updated", obj.getDescription(), updatedObject.getDescription());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        CnSIssue obj = new CnSIssue();
        obj.setDescription("description");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/cnSIssues/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/cnSIssues/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());

        // verify the object has been deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/cnSIssues/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/cnSIssues/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/cnSIssues")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<CnSIssue> result = readSearchResult(json, CnSIssue.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "createdDate";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/cnSIssues?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<CnSIssue> result = readSearchResult(json, CnSIssue.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected CnSIssue generateTestObject() throws Exception {
        CnSIssue cnSIssue = new CnSIssue();
        cnSIssue.setCreatedDate(new Date());
        cnSIssue.setRead(false);
        cnSIssue.setCreatedBy(getUser().getUsername());
        cnSIssue.setActionPlan("action plan");
        cnSIssue.setCriticalRoleLeadership(false);
        cnSIssue.setDivisionId(division.getId());
        cnSIssue.setEngineerLeader("leader");
        cnSIssue.setDescription("description it is.");
        cnSIssue.setEngineerReviewDate(new Date());
        cnSIssue.setEstimatedCompletionDate(new Date());
        cnSIssue.setExternalPartners("partners");
        cnSIssue.setFinancialImpact("impact");
        cnSIssue.setImpactSummary("summary");
        cnSIssue.setPriority(1);
        cnSIssue.setVpgmReviewDate(new Date());
        cnSIssue.setSuccessionPlan("plan");
        cnSIssue.setStandardId(standard.getId());
        return cnSIssue;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected CnSIssue createTestObject(CnSIssue obj) throws Exception {
        return cnSIssueService.create(obj);
    }

}
