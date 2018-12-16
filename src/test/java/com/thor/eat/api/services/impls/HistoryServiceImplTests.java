/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.History;
import com.thor.eat.api.entities.HistorySearchCriteria;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.RecordType;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.HistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for HistoryServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class HistoryServiceImplTests extends BaseTests<History> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private HistoryService historyService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        History testObject = getTestObject();
        History result = historyService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getOperation(), result.getOperation());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        History created = createTestObject(generateTestObject());
        History fetched = historyService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getOperation(), fetched.getOperation());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        History updateObject = new History();
        // update some fields
        updateObject.setOperation(OperationType.Update);
        historyService.update(id, updateObject);
        History updatedObject = historyService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getOperation(), updatedObject.getOperation());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        historyService.delete(id);
        historyService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        HistorySearchCriteria criteria = new HistorySearchCriteria();
        List<History> result = historyService.search(criteria);
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
        HistorySearchCriteria criteria = new HistorySearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<History> result = historyService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

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
