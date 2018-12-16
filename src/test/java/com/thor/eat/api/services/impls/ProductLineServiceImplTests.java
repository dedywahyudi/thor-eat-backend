/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineSearchCriteria;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.ProductLineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for ProductLineServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductLineServiceImplTests extends BaseTests<ProductLine> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private ProductLineService productLineService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        ProductLine testObject = getTestObject();
        ProductLine result = productLineService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getName(), result.getName());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        ProductLine created = createTestObject(generateTestObject());
        ProductLine fetched = productLineService.get(created.getId());

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
        ProductLine updateObject = new ProductLine();
        // update some fields
        updateObject.setName("updated-name");
        productLineService.update(id, updateObject);
        ProductLine updatedObject = productLineService.get(id);

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
        productLineService.delete(id);
        productLineService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        ProductLineSearchCriteria criteria = new ProductLineSearchCriteria();
        List<ProductLine> result = productLineService.search(criteria);
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
        ProductLineSearchCriteria criteria = new ProductLineSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<ProductLine> result = productLineService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

    }


    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ProductLine generateTestObject() throws Exception {
        ProductLine productLine = new ProductLine();
        productLine.setName("name");
        productLine.setSubDivisionId(subDivision.getId());
        productLine.setDivisionId(division.getId());
        return productLine;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ProductLine createTestObject(ProductLine obj) throws Exception {
        return productLineService.create(obj);
    }
}
