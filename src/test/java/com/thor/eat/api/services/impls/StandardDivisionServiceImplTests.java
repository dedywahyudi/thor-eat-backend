/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.StandardDivisionSearchCriteria;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.StandardDivisionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for StandardDivisionServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardDivisionServiceImplTests extends BaseTests<StandardDivision> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private StandardDivisionService standardDivisionService;


    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        StandardDivision testObject = getTestObject();
        StandardDivision result = standardDivisionService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getComment(), result.getComment());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        StandardDivision created = createTestObject(generateTestObject());
        StandardDivision fetched = standardDivisionService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getComment(), fetched.getComment());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        StandardDivision updateObject = new StandardDivision();
        // update some fields
        updateObject.setComment("updated comment");
        standardDivisionService.update(id, updateObject);
        StandardDivision updatedObject = standardDivisionService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getComment(), updatedObject.getComment());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        standardDivisionService.delete(id);
        standardDivisionService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        StandardDivisionSearchCriteria criteria = new StandardDivisionSearchCriteria();
        List<StandardDivision> result = standardDivisionService.search(criteria);
        assertEquals("should contain 1 element", 2, result.size());
    }

    /**
     * Tests the search method with pagination.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithPagination() throws Exception {
        // add one more object
        createTestObject(generateTestObject());
        StandardDivisionSearchCriteria criteria = new StandardDivisionSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<StandardDivision> result = standardDivisionService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 3 element in total", 3, result.getTotal());

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
