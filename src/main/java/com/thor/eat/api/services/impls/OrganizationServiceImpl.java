/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.OrganizationSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.OrganizationService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of OrganizationService,
 * extends BaseService<Organization, OrganizationSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class OrganizationServiceImpl
        extends BaseService<Organization, OrganizationSearchCriteria> implements OrganizationService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<Organization> getSpecification(OrganizationSearchCriteria criteria)
            throws ThorEarlyException {
        return new OrganizationSpecification(criteria);
    }
}

