/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.DivisionRepository;
import com.thor.eat.api.services.ProductLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of ProductLineService,
 * extends BaseService<ProductLine, ProductLineSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class ProductLineServiceImpl
        extends BaseService<ProductLine, ProductLineSearchCriteria> implements ProductLineService {

    /**
     * Represents the division repository.
     */
    @Autowired
    private DivisionRepository divisionRepository;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<ProductLine> getSpecification(ProductLineSearchCriteria criteria)
            throws ThorEarlyException {
        return new ProductLineSpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    protected void interceptCreateOrUpdate(ProductLine entity, ProductLine existing) throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);
        validateDivisionIdReference(entity.getDivisionId(), "divisionId", divisionRepository, existing != null);
        validateDivisionIdReference(entity.getSubDivisionId(), "subDivisionId", divisionRepository, true);

    }
}

