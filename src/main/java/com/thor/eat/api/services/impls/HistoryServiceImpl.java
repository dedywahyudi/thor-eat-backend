/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.History;
import com.thor.eat.api.entities.HistorySearchCriteria;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.UserRepository;
import com.thor.eat.api.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The Spring Data JPA implementation of HistoryService,
 * extends BaseService<History, HistorySearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class HistoryServiceImpl
        extends BaseService<History, HistorySearchCriteria> implements HistoryService {

    /**
     * Represents the user repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<History> getSpecification(HistorySearchCriteria criteria)
            throws ThorEarlyException {
        return new HistorySpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    protected void interceptCreateOrUpdate(History entity, History existing) throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);
        entity.setModifiedDate(new Date());
        validateReference(entity, "user", userRepository, User.class, existing != null);
    }
}

