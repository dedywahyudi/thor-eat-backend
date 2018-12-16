/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.repositories.PendingStandardRepository;
import com.thor.eat.api.services.ChangeRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class changeRequest apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ChangeRequestControllerTests extends BaseControllerTests<ChangeRequest> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private ChangeRequestService changeRequestService;

    /**
     * Represents the pending standard repository.
     */
    @Autowired
    private PendingStandardRepository pendingStandardRepository;


    /**
     * Tests the  api to approve.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void approve() throws Exception {
        long id = getTestObject().getId();
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/changeRequests/" + id + "/approve")
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    /**
     * Tests the  api to reject.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void reject() throws Exception {
        long id = getTestObject().getId();
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/changeRequests/" + id + "/reject")
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }


    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/changeRequests")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<ChangeRequest> result = readSearchResult(json, ChangeRequest.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "standardId";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/changeRequests?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<ChangeRequest> result = readSearchResult(json, ChangeRequest.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ChangeRequest generateTestObject() throws Exception {
        PendingStandard pendingStandard = new PendingStandard();
        pendingStandard.setName("standard");
        pendingStandard.setOrganization(organization);
        pendingStandard.setCreatedDate(new Date());
        pendingStandard.setCreatedBy(getUser().getId());
        pendingStandard.setEdition("test-edition");
        pendingStandard.setDate(new Date());
        pendingStandard.setNewDivisionId(standardDivision.getId());

        pendingStandard = pendingStandardRepository.save(pendingStandard);

        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setStandardId(null);
        changeRequest.setPendingStandard(pendingStandard);
        changeRequest.setRequestedDate(new Date());
        changeRequest.setRequestedUser(getUser());
        changeRequest.setType(OperationType.Insert);
        return changeRequest;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ChangeRequest createTestObject(ChangeRequest obj) throws Exception {
        return changeRequestService.create(obj);
    }

}
