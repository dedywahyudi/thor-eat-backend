/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.ProductLineContactSearchCriteria;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.ProductLineRepository;
import com.thor.eat.api.services.ProductLineContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of ProductLineContactService,
 * extends BaseService<ProductLineContact, ProductLineContactSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class ProductLineContactServiceImpl
        extends BaseService<ProductLineContact, ProductLineContactSearchCriteria> implements ProductLineContactService {

    /**
     * Represents the product line repository.
     */
    @Autowired
    private ProductLineRepository productLineRepository;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<ProductLineContact> getSpecification(ProductLineContactSearchCriteria criteria)
            throws ThorEarlyException {
        return new ProductLineContactSpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    protected void interceptCreateOrUpdate(ProductLineContact entity, ProductLineContact existing) throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);
        validateReference(entity, "productLine", productLineRepository, ProductLine.class, existing != null);
    }
}

