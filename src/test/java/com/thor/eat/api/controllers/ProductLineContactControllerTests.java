/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.services.ProductLineContactService;
import com.thor.eat.api.services.ProductLineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is the unit tests for class productLineContact apis.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductLineContactControllerTests extends BaseControllerTests<ProductLineContact> {
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
     * Tests the get api.
     */
    @Test
    public void get() throws Exception {
        ProductLineContact obj = getTestObject();
        long id = obj.getId();
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/productLineContacts/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ProductLineContact fetchedObj = objectMapper.readValue(json, ProductLineContact.class);

        // verify they are equal
        assertEquals("operation should be equal", obj.getContactEmail(), fetchedObj.getContactEmail());
    }

    /**
     * Tests the create api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void create() throws Exception {
        ProductLineContact obj = generateTestObject();
        ObjectNode node = (ObjectNode)(objectMapper.valueToTree(obj));
        node.putPOJO("productLine", obj.getProductLine());
        String json = objectMapper.writeValueAsString(node);

        String createdJson = mockAuthMvc.perform(MockMvcRequestBuilders.post("/productLineContacts")
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        ProductLineContact createdObj = objectMapper.readValue(createdJson, ProductLineContact.class);

        assertNotNull("id should be generated.", createdObj.getId());

        // verify they are equal
        assertEquals("operation should be equal", obj.getContactEmail(), createdObj.getContactEmail());
    }

    /**
     * Tests the create api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void createAuthorization() throws Exception {
        ProductLineContact obj = generateTestObject();
        String json = objectMapper.writeValueAsString(obj);

        mockAuthMvc.perform(MockMvcRequestBuilders.post("/productLineContacts")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isForbidden());
    }


    /**
     * Tests the update api.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        ProductLineContact obj = new ProductLineContact();
        obj.setContactEmail("updated@tc.com");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        String updatedJson = mockAuthMvc.perform(MockMvcRequestBuilders.put("/productLineContacts/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ProductLineContact updatedObject = objectMapper.readValue(updatedJson, ProductLineContact.class);

        // verify that it is updated
        assertEquals("should be updated", obj.getContactEmail(), updatedObject.getContactEmail());
    }

    /**
     * Tests the update api without authorization.
     *
     * @throws Exception if there are any errors.
     */
    @Test
    public void updateAuthorization() throws Exception {
        long id = getTestObject().getId();
        ProductLineContact obj = new ProductLineContact();
        obj.setContactEmail("updated@tc.com");

        String updateJson = objectMapper.writeValueAsString(obj);
        // update the obj
        mockAuthMvc.perform(MockMvcRequestBuilders.put("/productLineContacts/" + id)
                .contentType(MediaType.APPLICATION_JSON).content(updateJson))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the delete api.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void delete() throws Exception {
        long id = getTestObject().getId();

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/productLineContacts/" + id)
                .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());

        // verify the object has been deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/productLineContacts/" + id)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the delete api without authorization.
     * @throws Exception if there are any exceptions.
     */
    @Test
    public void deleteAuthorization() throws Exception {
        long id = getTestObject().getId();

        mockAuthMvc.perform(MockMvcRequestBuilders.delete("/productLineContacts/" + id))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/productLineContacts")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<ProductLineContact> result = readSearchResult(json, ProductLineContact.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
    }

    /**
     * Tests the search api.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithSortColumn() throws Exception {
        final String sortColumn = "contactEmail";
        String json = mockMvc.perform(MockMvcRequestBuilders.get("/productLineContacts?sortColumn=" + sortColumn)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SearchResult<ProductLineContact> result = readSearchResult(json, ProductLineContact.class);
        assertEquals("should contain 1 element", 1, result.getItems().size());
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
        productLineContact.setProductLine(this.productLine);
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
