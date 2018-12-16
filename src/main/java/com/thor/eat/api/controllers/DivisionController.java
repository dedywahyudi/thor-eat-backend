/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.controllers;

import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.responses.DivisionStatistics;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.DivisionService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.groups.Default;

import java.util.List;
import java.util.Map;

/**
 * The division REST controller. Is effectively thread safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RestController
@RequestMapping("/divisions")
@NoArgsConstructor
public class DivisionController {

    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private DivisionService divisionService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(divisionService, "divisionService");
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
    @RequestMapping(value = "{id:.+}", method = RequestMethod.GET)
    public Division get(@PathVariable String id) throws ThorEarlyException {
        return divisionService.get(id);
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return return all child of given division
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id:.+}/children", method = RequestMethod.GET)
    public List<Division> getChildren(@PathVariable String id) throws ThorEarlyException {
        return divisionService.getChildren(id);
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
    public Division create(@Validated({ValidationGroup.Create.class, Default.class}) @RequestBody Division entity)
            throws ThorEarlyException  {
        return divisionService.create(entity);
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
    @RequestMapping(value = "{id:.+}", method = RequestMethod.PUT)
    @Transactional
    public Division update(@PathVariable String id, @Validated(Default.class) @RequestBody Division entity)
            throws ThorEarlyException  {
        return divisionService.update(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id:.+}", method = RequestMethod.DELETE)
    @Transactional
    public void delete(@PathVariable String id) throws ThorEarlyException  {
        divisionService.delete(id);
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
    public SearchResult<Division> search(@ModelAttribute DivisionSearchCriteria criteria,
                                      @ModelAttribute Paging paging) throws ThorEarlyException  {
        if (paging.getSortColumn() == null) {
            paging.setSortColumn("id");
        }
        return divisionService.search(criteria, paging);
    }

    /**
     * Get region statistics base on standard id.
     *
     * @param id the standard id.
     * @return the region statistics.
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistics/{id}")
    public Map<String, List<DivisionStatistics>> getRegionStatistics(@PathVariable Long id) throws ThorEarlyException {
    	return divisionService.getRegionStatistics(id);
    }
}

