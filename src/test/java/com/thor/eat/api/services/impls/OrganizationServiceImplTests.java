/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.OrganizationSearchCriteria;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.OrganizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for OrganizationServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class OrganizationServiceImplTests extends BaseTests<Organization> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private OrganizationService organizationService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        Organization testObject = getTestObject();
        Organization result = organizationService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getName(), result.getName());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        Organization created = createTestObject(generateTestObject());
        Organization fetched = organizationService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getName(), fetched.getName());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        Organization updateObject = new Organization();
        // update some fields
        updateObject.setName("updated name");
        organizationService.update(id, updateObject);
        Organization updatedObject = organizationService.get(id);

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
        long id = getTestObject().getId();
        organizationService.delete(id);
        organizationService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        OrganizationSearchCriteria criteria = new OrganizationSearchCriteria();
        List<Organization> result = organizationService.search(criteria);
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
        OrganizationSearchCriteria criteria = new OrganizationSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<Organization> result = organizationService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 3 element in total", 3, result.getTotal());

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
