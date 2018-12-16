/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.services.OrganizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class organization apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class OrganizationControllerTests extends BaseControllerTests<Organization> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private OrganizationService organizationService;

    /**
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        Organization obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/organizations/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Organization fetchedObj = objectMapper.readValue(json, Organization.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getName(), fetchedObj.getName());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        Organization obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/organizations")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        Organization createdObj = objectMapper.readValue(createdJson, Organization.class);

        assertNotNull("id should be generated.", createdObj.getId());

        // verify they are equal
        assertEquals("operation should be equal", obj.getName(), createdObj.getName());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        Organization obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/organizations")
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
        Organization obj = new Organization();
        obj.setName("updated name");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/organizations/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Organization updatedObject = objectMapper.readValue(updatedJson, Organization.class);

        // verify that it is updated
        assertEquals("should be updated", obj.getName(), updatedObject.getName());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        Organization obj = new Organization();
        obj.setName("Updated name");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/organizations/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/organizations/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());

        // verify the object has been deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/organizations/" + id)
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

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/organizations/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/organizations")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Organization> result = readSearchResult(json, Organization.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "name";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/organizations?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Organization> result = readSearchResult(json, Organization.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Organization generateTestObject() throws Exception {
        Organization organization = new Organization();
        organization.setName("test org");
        return organization;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Organization createTestObject(Organization obj) throws Exception {
        return organizationService.create(obj);
    }

}
