/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.controllers;

import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.Standard;
import com.thor.eat.api.entities.StandardSearchCriteria;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.StandardService;
import com.thor.eat.api.utils.Helper;
import com.thor.eat.api.utils.ValidationGroup;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.groups.Default;

/**
 * The standard REST controller. Is effectively thread safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RestController
@RequestMapping("/standards")
@NoArgsConstructor
public class StandardController {

    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private StandardService standardService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(standardService, "standardService");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Standard get(@PathVariable Long id) throws ThorEarlyException {
        return standardService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ChangeRequest createPendingStandard(@Validated({ValidationGroup.Create.class, Default.class})
                                                   @RequestBody PendingStandard entity)
            throws ThorEarlyException  {
        return standardService.processCreateRequest(entity);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is null or empty or entity is null or id of entity is null or empty.
     * or id of entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @Transactional
    public ChangeRequest update(@PathVariable Long id, @Validated(Default.class) @RequestBody PendingStandard entity)
            throws ThorEarlyException  {
        return standardService.processUpdateRequest(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @Transactional
    public ChangeRequest delete(@PathVariable String id,
    		@RequestParam(value="type", required=false, defaultValue="standard") String type) throws ThorEarlyException  {
        return standardService.processDeleteRequest(id, type);
    }

    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<Standard> search(@ModelAttribute StandardSearchCriteria criteria,
                                      @ModelAttribute Paging paging) throws ThorEarlyException  {
        if (paging.getSortColumn() == null) {
            paging.setSortColumn("name asc");
        }
        return standardService.search(criteria, paging);
    }
}

