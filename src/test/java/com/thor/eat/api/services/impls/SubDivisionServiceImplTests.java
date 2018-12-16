/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.*;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.DivisionService;
import com.thor.eat.api.services.SubDivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for SubDivisionServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SubDivisionServiceImplTests extends BaseTests<Division> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private SubDivisionService subDivisionService;

    /**
     * Represents DivisionService instance
     */
    @Autowired
    private DivisionService divisionService;

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        DivisionSearchCriteria criteria = new DivisionSearchCriteria();
        List<Division> result = subDivisionService.search(criteria);
        assertEquals("should contain 2 element", 2, result.size());
    }

    /**
     * Tests the search method with pagination.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithPagination() throws Exception {
        DivisionSearchCriteria criteria = new DivisionSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<Division> result = subDivisionService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

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
