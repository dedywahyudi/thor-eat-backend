/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.controllers;

import com.thor.eat.api.entities.Role;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.RoleRepository;
import com.thor.eat.api.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * The role REST controller. Is effectively thread safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RestController
@RequestMapping("/roles")
@NoArgsConstructor
public class RoleController {

    /**
     * Represents the repository of the roles.
     */
    @Autowired
    private RoleRepository repository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
    }

    /**
     * This method is used to search for entities by criteria and paging params.
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Role> search() throws ThorEarlyException  {
        return repository.findAll();
    }
}

