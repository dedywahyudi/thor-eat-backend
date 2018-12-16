/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.DivisionRepository;
import com.thor.eat.api.services.SubDivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of SubDivisionService,
 * extends BaseService<Division, DivisionSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class SubDivisionServiceImpl
        extends SearchBaseService<Division, DivisionSearchCriteria> implements SubDivisionService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<Division> getSpecification(DivisionSearchCriteria criteria)
            throws ThorEarlyException {
        return new SubDivisionSpecification(criteria);
    }
}

