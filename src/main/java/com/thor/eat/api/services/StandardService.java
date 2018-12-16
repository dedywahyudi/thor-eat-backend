/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.Standard;
import com.thor.eat.api.entities.StandardSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;

import javax.transaction.Transactional;

/**
 * The standard service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface StandardService extends GenericService<Standard, StandardSearchCriteria> {
    @Transactional
    ChangeRequest processCreateRequest(PendingStandard standard) throws ThorEarlyException;

    @Transactional
    ChangeRequest processUpdateRequest(long standardId, PendingStandard standard) throws ThorEarlyException;

    @Transactional
    ChangeRequest processDeleteRequest(String standardId, String type) throws ThorEarlyException;
}

