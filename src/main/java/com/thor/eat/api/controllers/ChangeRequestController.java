/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.controllers;

import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.ChangeRequestAction;
import com.thor.eat.api.entities.ChangeRequestSearchCriteria;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.ChangeRequestService;
import com.thor.eat.api.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * The change request REST controller. Is effectively thread safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@RestController
@RequestMapping("/changeRequests")
@NoArgsConstructor
public class ChangeRequestController {

    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private ChangeRequestService changeRequestService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(changeRequestService, "changeRequestService");
    }



    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param action the action.
     * @return the updated entity
     * @throws IllegalArgumentException if id is null or empty or entity is null or id of entity is null or empty.
     * or id of entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/{action}", method = RequestMethod.PUT)
    @Transactional
    public List<String> handleAction(@PathVariable Long id, @PathVariable ChangeRequestAction action, @RequestBody(required=false) String rejectMessage)
            throws ThorEarlyException  {
        if (action == ChangeRequestAction.approve) {
            return changeRequestService.approveChangeRequest(id);
        } else {
            return changeRequestService.rejectChangeRequest(id, rejectMessage);
        }
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
    public SearchResult<ChangeRequest> search(@ModelAttribute ChangeRequestSearchCriteria criteria,
                                      @ModelAttribute Paging paging) throws ThorEarlyException  {
        if (paging.getSortColumn() == null) {
            paging.setSortColumn("requestedDate desc");
        }
        return changeRequestService.search(criteria, paging);
    }
}

