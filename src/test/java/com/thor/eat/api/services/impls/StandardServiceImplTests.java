/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.*;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.StandardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for StandardServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardServiceImplTests extends BaseTests<Standard> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private StandardService standardService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        Standard testObject = getTestObject();
        Standard result = standardService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getDescription(), result.getDescription());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        Standard created = createTestObject(generateTestObject());
        Standard fetched = standardService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getDescription(), fetched.getDescription());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        Standard updateObject = new Standard();
        // update some fields
        updateObject.setDescription("updated description");
        standardService.update(id, updateObject);
        Standard updatedObject = standardService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getDescription(), updatedObject.getDescription());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        standardService.delete(id);
        standardService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        StandardSearchCriteria criteria = new StandardSearchCriteria();
        List<Standard> result = standardService.search(criteria);
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
        StandardSearchCriteria criteria = new StandardSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<Standard> result = standardService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 3 element in total", 3, result.getTotal());
    }

    /**
     * Tests the processCreateRequest method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void processCreateRequest() throws Exception {
        PendingStandard standard = new PendingStandard();
        ChangeRequest changeRequest = standardService.processCreateRequest(standard);
        assertEquals("change request should be created", OperationType.Insert, changeRequest.getType());
    }

    /**
     * Tests the processDeleteRequest method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void processDeleteRequest() throws Exception {
        ChangeRequest changeRequest = standardService.processDeleteRequest(standardDivision.getId() + "", "standard");
        assertEquals("change request should be created", OperationType.Delete, changeRequest.getType());
    }

    /**
     * Tests the processUpdateRequest method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void processUpdateRequest() throws Exception {
        PendingStandard updatedStandard = new PendingStandard();
        updatedStandard.setDescription("updated description");
        ChangeRequest changeRequest = standardService.processUpdateRequest(standardDivision.getId(), updatedStandard);
        assertEquals("change request should be created", OperationType.Update, changeRequest.getType());
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
