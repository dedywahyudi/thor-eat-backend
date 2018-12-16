/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.StandardDivisionSearchCriteria;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;

/**
 * The standard division service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface StandardDivisionService extends GenericService<StandardDivision, StandardDivisionSearchCriteria> {
}

