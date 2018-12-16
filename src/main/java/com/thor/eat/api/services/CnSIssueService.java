/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.CnSIssueSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;

import javax.transaction.Transactional;

/**
 * The cns issue service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface CnSIssueService extends GenericService<CnSIssue, CnSIssueSearchCriteria> {
    @Transactional
    CnSIssue markRead(long id) throws ThorEarlyException;

    public void fillDivisionName(CnSIssue cnSIssue);
}

