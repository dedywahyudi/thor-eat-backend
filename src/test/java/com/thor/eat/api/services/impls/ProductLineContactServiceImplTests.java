/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.ProductLineContactSearchCriteria;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.ProductLineContactService;
import com.thor.eat.api.services.ProductLineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for ProductLineContactServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductLineContactServiceImplTests extends BaseTests<ProductLineContact> {
    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private ProductLineContactService productLineContactService;

    /**
     * Represents the product line service.
     */
    @Autowired
    private ProductLineService productLineService;

    /**
     * Represents the product line.
     */
    private ProductLine productLine;

    /**
     * Prepare the data for tests.
     * @throws Exception if there are any errors.
     */
    @Override
    public void prepareData() throws Exception {
        super.prepareData();
        ProductLine productLine = new ProductLine();
        productLine.setName("product line");
        productLine.setDivisionId(division.getId());
        productLine.setSubDivisionId(subDivision.getId());
        this.productLine = productLineService.create(productLine);
    }

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        ProductLineContact testObject = getTestObject();
        ProductLineContact result = productLineContactService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getContactEmail(), result.getContactEmail());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        ProductLineContact created = createTestObject(generateTestObject());
        ProductLineContact fetched = productLineContactService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getContactEmail(), fetched.getContactEmail());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        ProductLineContact updateObject = new ProductLineContact();
        // update some fields
        updateObject.setContactEmail("updated@tc.com");
        productLineContactService.update(id, updateObject);
        ProductLineContact updatedObject = productLineContactService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getContactEmail(), updatedObject.getContactEmail());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        productLineContactService.delete(id);
        productLineContactService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        ProductLineContactSearchCriteria criteria = new ProductLineContactSearchCriteria();
        List<ProductLineContact> result = productLineContactService.search(criteria);
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
        ProductLineContactSearchCriteria criteria = new ProductLineContactSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<ProductLineContact> result = productLineContactService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 2 element in total", 2, result.getTotal());

    }


    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ProductLineContact generateTestObject() throws Exception {
        ProductLineContact productLineContact = new ProductLineContact();
        productLineContact.setContactEmail("test@tc.com");
        productLineContact.setProductLine(productLine);
        return productLineContact;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected ProductLineContact createTestObject(ProductLineContact obj) throws Exception {
        return productLineContactService.create(obj);
    }
}
