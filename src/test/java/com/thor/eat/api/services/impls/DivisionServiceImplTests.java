/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.DivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for DivisionServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DivisionServiceImplTests extends BaseTests<Division> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private DivisionService divisionService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        Division testObject = getTestObject();
        Division result = divisionService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getName(), result.getName());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        Division created = createTestObject(generateTestObject());
        Division fetched = divisionService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getName(), fetched.getName());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        String id = getTestObject().getId();
        Division updateObject = new Division();
        // update some fields
        updateObject.setName("updated-name");
        divisionService.update(id, updateObject);
        Division updatedObject = divisionService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getName(), updatedObject.getName());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        String id = getTestObject().getId();
        divisionService.delete(id);
        divisionService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        DivisionSearchCriteria criteria = new DivisionSearchCriteria();
        List<Division> result = divisionService.search(criteria);
        assertEquals("should contain 2 element", 2, result.size());
    }

    /**
     * Tests the search method with pagination.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithPagination() throws Exception {
        // add one more object
        createTestObject(generateTestObject());
        DivisionSearchCriteria criteria = new DivisionSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<Division> result = divisionService.search(criteria, paging);
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
        Division division = new Division();
        division.setId("1.2");
        division.setName("test-name");
        division.setRegion("test-region");
        return division;
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
