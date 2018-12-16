/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.ChangeRequestSearchCriteria;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.repositories.PendingStandardRepository;
import com.thor.eat.api.services.ChangeRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for ChangeRequestServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ChangeRequestServiceImplTests extends BaseTests<ChangeRequest> {
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
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        ChangeRequest testObject = getTestObject();
        ChangeRequest result = changeRequestService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getType(), result.getType());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        ChangeRequest created = createTestObject(generateTestObject());
        ChangeRequest fetched = changeRequestService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getType(), fetched.getType());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        ChangeRequest updateObject = new ChangeRequest();
        // update some fields
        updateObject.setType(OperationType.Update);
        changeRequestService.update(id, updateObject);
        ChangeRequest updatedObject = changeRequestService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getType(), updatedObject.getType());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        changeRequestService.delete(id);
        changeRequestService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        ChangeRequestSearchCriteria criteria = new ChangeRequestSearchCriteria();
        List<ChangeRequest> result = changeRequestService.search(criteria);
        assertEquals("should contain 1 element", 1, result.size());
    }

    /**
     * Tests the search method with pagination.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithPagination() throws Exception {
        // add one more object
        createTestObject(generateTestObject());
        ChangeRequestSearchCriteria criteria = new ChangeRequestSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<ChangeRequest> result = changeRequestService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

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
