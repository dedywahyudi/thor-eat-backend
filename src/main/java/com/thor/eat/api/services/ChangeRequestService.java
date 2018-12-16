/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.ChangeRequestSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;

import java.util.List;

import javax.transaction.Transactional;

/**
 * The change request service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface ChangeRequestService extends GenericService<ChangeRequest, ChangeRequestSearchCriteria> {
    @Transactional
    List<String> approveChangeRequest(long changeRequestId) throws ThorEarlyException;

    @Transactional
    List<String> rejectChangeRequest(long changeRequestId, String rejectMessage) throws ThorEarlyException;
}

