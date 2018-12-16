/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;

import com.thor.eat.api.BaseTests;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.UserSearchCriteria;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is the test cases for UserServiceImpl.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTests extends BaseTests<User> {
    /**
     * The environment variables
     */
    @Autowired
    private Environment env;

    /**
     * Represents the service for unit tests.
     */
    @Autowired
    private UserService userService;

    /**
     * Tests the get method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void get() throws Exception {
        User testObject = getTestObject();
        User result = userService.get(testObject.getId());

        // verify the fields
        assertEquals("operation should be equal.", testObject.getUsername(), result.getUsername());
    }

    /**
     * Tests the create method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void create() throws Exception {
        User tmp = generateTestObject();
        tmp.setUsername("newname");
        tmp.setEmail("newemail@test.com");
        User created = createTestObject(tmp);
        User fetched = userService.get(created.getId());

        // verify to be equal
        assertEquals("operation should be equal.", created.getUsername(), fetched.getUsername());
    }

    /**
     * Tests the update method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void update() throws Exception {
        long id = getTestObject().getId();
        User updateObject = new User();
        // update some fields
        updateObject.setUsername("username");
        userService.update(id, updateObject);
        User updatedObject = userService.get(id);

        // verify to be equal
        assertEquals("operation should be equal.",
                updateObject.getUsername(), updatedObject.getUsername());
    }

    /**
     * Tests the delete method.
     * @throws Exception if there are any errors.
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete() throws Exception {
        long id = getTestObject().getId();
        userService.delete(id);
        userService.get(id);
    }

    /**
     * Tests the search method.
     * @throws Exception if there are any errors.
     */
    @Test
    public void search() throws Exception {
        UserSearchCriteria criteria = new UserSearchCriteria();
        List<User> result = userService.search(criteria);
        assertEquals("should contain 2 element", 2, result.size());
    }

    /**
     * Tests the search method with pagination.
     * @throws Exception if there are any errors.
     */
    @Test
    public void searchWithPagination() throws Exception {
        // add one more object
        User tmp = generateTestObject();
        tmp.setUsername("newname");
        tmp.setEmail("newemail@test.com");
        createTestObject(tmp);
        UserSearchCriteria criteria = new UserSearchCriteria();
        Paging paging = new Paging();
        paging.setLimit(1);
        SearchResult<User> result = userService.search(criteria, paging);
        assertEquals("should contain 1 element", 1, result.getItems().size());
        assertEquals("should have 3 element in total", 3, result.getTotal());

    }


    /**
     * Helper method to generate the test object.
     * @return the test object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected User generateTestObject() throws Exception {
        User user = new User();
        user.setPassword(this.env.getProperty("password.user"));
        user.setUsername("testaccount");
        user.setRole(role);
        user.setFullName("test account");
        user.setEmail("test@test.com");
        return user;
    }

    /**
     * Create the test object.
     * @param obj the object.
     * @return the object.
     * @throws Exception if there are any errors.
     */
    @Override
    protected User createTestObject(User obj) throws Exception {
        return userService.create(obj);
    }
}
