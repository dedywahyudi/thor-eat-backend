/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.services.DivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class subDivision apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SubDivisionControllerTests extends BaseControllerTests<Division> {

    /**
     * Represents DivisionService instance
     */
    @Autowired
    private DivisionService divisionService;

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/subDivisions")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Division> result = readSearchResult(json, Division.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "name";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/subDivisions?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<Division> result = readSearchResult(json, Division.class);
        assertEquals("should contain 2 element", 2, result.getItems().size());
    }

    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Division generateTestObject() throws Exception {
        Division subDivision = new Division();
        subDivision.setId("1.20");
        subDivision.setParentDivision(division);
        subDivision.setName("test sub division");
        subDivision.setRegion("test regison");
        return subDivision;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected Division createTestObject(Division obj) throws Exception {
        return divisionService.create(obj);
    }

}
