/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.*;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.DivisionRepository;
import com.thor.eat.api.repositories.ProductLineContactRepository;
import com.thor.eat.api.repositories.ProductLineRepository;
import com.thor.eat.api.repositories.StandardRepository;
import com.thor.eat.api.services.StandardDivisionService;
import com.thor.eat.api.utils.Helper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * The Spring Data JPA implementation of StandardDivisionService,
 * extends BaseService<StandardDivision, StandardDivisionSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class StandardDivisionServiceImpl
        extends BaseService<StandardDivision, StandardDivisionSearchCriteria> implements StandardDivisionService {

    /**
     * Represents the standard repository
     */
    @Autowired
    private StandardRepository standardRepository;

    /**
     * Represents the product line repository
     */
    @Autowired
    private ProductLineRepository productLineRepository;

    /**
     * Represents the division repository
     */
    @Autowired
    private DivisionRepository divisionRepository;

    /**
     * Represents the product line contact repository.
     */
    @Autowired
    private ProductLineContactRepository productLineContactRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(standardRepository, "standardRepository");
        Helper.checkConfigNotNull(productLineRepository, "productLineRepository");
        Helper.checkConfigNotNull(divisionRepository, "divisionRepository");
        Helper.checkConfigNotNull(productLineContactRepository, "productLineContactRepository");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<StandardDivision> getSpecification(StandardDivisionSearchCriteria criteria)
            throws ThorEarlyException {
        return new StandardDivisionSpecification(criteria, productLineContactRepository);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    protected void interceptCreateOrUpdate(StandardDivision entity, StandardDivision existing)
            throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);

        if (existing == null) {
        	List<ProductLineContact> newContacts = new ArrayList<>();
        	if (entity.getProductLine() != null) {
	        	// Create new contact
	        	for (ProductLineContact contact : entity.getProductLine().getProductLineContacts()) {
	        		ProductLineContact newContact = new ProductLineContact();
		        	newContact.setContactEmail(contact.getContactEmail());
		        	newContacts.add(newContact);
		        }

		        entity.getProductLine().getProductLineContacts().clear();
		        for (ProductLineContact contact : newContacts) {
		        	entity.getProductLine().addContact(contact);
		        }
        	}
        } else {
        	if (entity.getProductLine() != null) {
        		if (existing.getProductLine() == null) {
        			existing.setProductLine(new ProductLine());
        		}
        		BeanUtils.copyProperties(entity.getProductLine(), existing.getProductLine(), "productLineContacts", "id");
	        	// update contact
	        	for (ProductLineContact contact : existing.getProductLine().getProductLineContacts()) {
	        		productLineContactRepository.delete(contact);
	        	}
	        	existing.getProductLine().getProductLineContacts().clear();

        		for (ProductLineContact contact : entity.getProductLine().getProductLineContacts()) {
        			ProductLineContact newContact = new ProductLineContact();
    		        newContact.setContactEmail(contact.getContactEmail());
                    existing.getProductLine().addContact(newContact);
                }
        	}
            if (entity.getSubDivision() == null) {
                existing.setSubDivision(null);
            }
        }

        if (entity.getStandard() != null && entity.getStandard().getId() != 0) {
        	validateReference(entity, "standard", standardRepository, Standard.class, existing != null);
        }

        validateDivisionReference(entity, "division", divisionRepository, Division.class, existing != null);
//        validateReference(entity, "subDivision", subDivisionRepository, SubDivision.class, true);
//        validateReference(entity, "productLine", productLineRepository, ProductLine.class, true);
    }
}

