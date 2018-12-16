/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.PendingStandard;
import com.thor.eat.api.entities.PendingStandardSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.OrganizationRepository;
import com.thor.eat.api.services.PendingStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of PendingStandardService,
 * extends BaseService<PendingStandard, PendingStandardSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class PendingStandardServiceImpl
        extends BaseService<PendingStandard, PendingStandardSearchCriteria> implements PendingStandardService {

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<PendingStandard> getSpecification(PendingStandardSearchCriteria criteria)
            throws ThorEarlyException {
        return new PendingStandardSpecification(criteria);
    }

    @Override
    protected void interceptCreateOrUpdate(PendingStandard entity, PendingStandard existing) throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);
        validateReference(entity, "organization", organizationRepository, Organization.class, true);
    }
}

