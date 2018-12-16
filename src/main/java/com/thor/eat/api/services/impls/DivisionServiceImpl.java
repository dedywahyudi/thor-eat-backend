/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.Standard;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.responses.DivisionStatistics;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.DivisionRepository;
import com.thor.eat.api.repositories.ProductLineContactRepository;
import com.thor.eat.api.repositories.StandardDivisionRepository;
import com.thor.eat.api.repositories.StandardRepository;
import com.thor.eat.api.services.DivisionService;
import com.thor.eat.api.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Spring Data JPA implementation of DivisionService,
 * extends BaseService<Division, DivisionSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class DivisionServiceImpl
        extends SearchBaseService<Division, DivisionSearchCriteria> implements DivisionService {
    /**
     * The repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private DivisionRepository repository;

    /**
     * The standard division repository. Should be non-null after injection.
     */
    @Autowired
    private StandardDivisionRepository standardDivisionRepository;

    /**
     * The standard repository. Should be non-null after injection.
     */
    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private ProductLineContactRepository productLineContactRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
    }

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
        return new DivisionSpecification(criteria);
    }
    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    public Division get(String id) throws ThorEarlyException {
        return ensureEntityExist(id);
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return all the children of given division
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    public List<Division> getChildren(String id) throws ThorEarlyException {
        return repository.findByParentDivisionId(id);
    }
    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the existing entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     * @throws EntityNotFoundException if the entity does not exist
     */
    private Division checkUpdate(String id, Division entity) throws EntityNotFoundException {
        Helper.checkNullOrEmpty(id, "id");
        Helper.checkNull(entity, "entity");
        Helper.checkNullOrEmpty(entity.getId(), "entity.id");
        if (!entity.getId().equals(id)) {
            throw new IllegalArgumentException(
                    "The id in the entity is not consistent with the id in the request.");
        }
        return ensureEntityExist(id);
    }

    /**
     * Check whether an identifiable entity with a given id exists.
     *
     * @param id the id of entity
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the match entity can not be found in DB
     */
    private Division ensureEntityExist(String id) throws EntityNotFoundException {
        Helper.checkNullOrEmpty(id, "id");
        Division entity = repository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity not found for id: [" + id + "]");
        }
        return entity;
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public Division create(Division entity) throws ThorEarlyException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("id must be present when creating an entity.");
        }
        Helper.checkNull(entity, "entity");

        interceptCreateOrUpdate(entity, null);

        return repository.save(entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public void delete(String id) throws ThorEarlyException {
        Helper.checkNullOrEmpty(id, "id");
        ensureEntityExist(id);
        repository.delete(id);
    }


    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public Division update(String id, Division entity) throws ThorEarlyException {
        entity.setId(id);
        Division existing = checkUpdate(id, entity);
        interceptCreateOrUpdate(entity, existing);

        copyProperties(entity, existing);
        return repository.save(existing);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    private void interceptCreateOrUpdate(Division entity, Division existing) throws ThorEarlyException {

    }

    /**
     * Get region statistics base on standard id.
     * @param standardId the standard id.
     * @return the region statistics.
     * @throws ThorEarlyException if any other error occurred during operation.
     */
	@Override
	public Map<String, List<DivisionStatistics>> getRegionStatistics(Long standardId) throws ThorEarlyException {
		Helper.checkPositive(standardId, "standardId");
		Standard standard = standardRepository.findOne(standardId);
		Helper.checkEntity(standard, "standard");
		List<Division> divisions = repository.findAll();

		Map<String, List<DivisionStatistics>> regionStatistics = new HashMap<String, List<DivisionStatistics>>();
		for (Division division : divisions) {
			String region = division.getRegion();
			if (region != null && !region.trim().isEmpty() && division.getParentDivision() == null) {
				// the division is parent division
				if (!regionStatistics.containsKey(region)) {
					regionStatistics.put(region, new ArrayList<DivisionStatistics>());
				}

				DivisionStatistics ds = new DivisionStatistics();
				ds.setDivision(division);

				List<StandardDivision> sds = standardDivisionRepository.findByDivision(division.getId());
				List<ProductLineContact> sdcs = new ArrayList<ProductLineContact>();
				for (StandardDivision sd : sds) {
					if (standard.getName().equals(sd.getStandard().getName())) {
						ds.setChecked(true);
						if (sd.getProductLine() != null) {
							sdcs.addAll(productLineContactRepository.findAllByProductLine_Id(sd.getProductLine().getId()));
						}
					}
				}
				ds.setContacts(sdcs);
//				ds.setSubDivisions(getAllChildDivision(division, divisions));
				regionStatistics.get(region).add(ds);
			}
		}
		return regionStatistics;
	}

	/**
	 * Get all child division of current division.
	 * @param current the current division
	 * @param divisions the divisions.
	 * @return all child divisions.
	 */
	private List<Division> getAllChildDivision(Division current, List<Division> divisions) {
		List<Division> childDivisions = new ArrayList<Division>();
		for (Division division : divisions) {
			if (division.getParentDivision() != null && current.getId().equals(division.getParentDivision().getId())) {
				childDivisions.add(division);
				childDivisions.addAll(getAllChildDivision(division, divisions));
			}
		}
		return childDivisions;
	}
}

