/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.CnSIssueSearchCriteria;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.CnSIssueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for CnSIssueServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CnSIssueServiceImplTests extends BaseTests<CnSIssue> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private CnSIssueService cnSIssueService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        CnSIssue testObject = getTestObject();
        CnSIssue result = cnSIssueService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getDescription(), result.getDescription());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        CnSIssue created = createTestObject(generateTestObject());
        CnSIssue fetched = cnSIssueService.get(created.getId());

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
        CnSIssue updateObject = new CnSIssue();
        // update some fields
        updateObject.setDescription("updated description");
        cnSIssueService.update(id, updateObject);
        CnSIssue updatedObject = cnSIssueService.get(id);

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
        cnSIssueService.delete(id);
        cnSIssueService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        CnSIssueSearchCriteria criteria = new CnSIssueSearchCriteria();
        List<CnSIssue> result = cnSIssueService.search(criteria);
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
        CnSIssueSearchCriteria criteria = new CnSIssueSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<CnSIssue> result = cnSIssueService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

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
