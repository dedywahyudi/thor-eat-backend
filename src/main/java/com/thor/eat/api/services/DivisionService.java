/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.entities.responses.DivisionStatistics;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;

import java.util.List;
import java.util.Map;

/**
 * The division service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface DivisionService extends SearchGenericService<Division, DivisionSearchCriteria> {
    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    Division get(String id) throws ThorEarlyException;

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return all the children of given division
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    List<Division> getChildren(String id) throws ThorEarlyException;

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    Division create(Division entity) throws ThorEarlyException;

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     *                                  or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    Division update(String id, Division entity) throws ThorEarlyException;

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    void delete(String id)throws ThorEarlyException;

    /**
     * Get region statistics base on standard id.
     * @param standardId the standard id.
     * @return the region statistics.
     * @throws ThorEarlyException if any other error occurred during operation.
     */
    Map<String, List<DivisionStatistics>> getRegionStatistics(Long standardId) throws ThorEarlyException;

}

