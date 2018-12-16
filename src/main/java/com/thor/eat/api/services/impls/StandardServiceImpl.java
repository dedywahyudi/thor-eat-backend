/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.aop.LogAspect;
import com.thor.eat.api.entities.*;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.OrganizationRepository;
import com.thor.eat.api.services.ChangeRequestService;
import com.thor.eat.api.services.DivisionService;
import com.thor.eat.api.services.EmailService;
import com.thor.eat.api.services.PendingStandardService;
import com.thor.eat.api.services.StandardDivisionService;
import com.thor.eat.api.services.StandardService;
import com.thor.eat.api.utils.Helper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * The Spring Data JPA implementation of StandardService,
 * extends BaseService<Standard, StandardSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class StandardServiceImpl
        extends BaseService<Standard, StandardSearchCriteria> implements StandardService {

    /**
     * Represents the organization repository
     */
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PendingStandardService pendingStandardService;

    @Autowired
    private ChangeRequestService changeRequestService;

    @Autowired
    private StandardDivisionService standardDivisionService;
    
    @Autowired
    private DivisionService divisionService;

    @Autowired
    private EmailService emailService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(organizationRepository, "organizationRepository");
        Helper.checkConfigNotNull(pendingStandardService, "pendingStandardService");
        Helper.checkConfigNotNull(changeRequestService, "changeRequestService");
    }


    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<Standard> getSpecification(StandardSearchCriteria criteria)
            throws ThorEarlyException {
        return new StandardSpecification(criteria);
    }


    /**
     * This method is used to handle the create operation.
     *
     * @param standard the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public ChangeRequest processCreateRequest(PendingStandard standard) throws ThorEarlyException {
        standard.setCreatedBy(Helper.getAuthUser().getId());
        standard.setCreatedDate(new Date());

        // create it to pending standard table
        PendingStandard pendingStandard = pendingStandardService.create(standard);

        // create the change request
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setType(OperationType.Insert);
        if (standard.getOrganization() != null) {
            changeRequest.setRecordType(RecordType.Standard);
        } else if (standard.getDivisionId() != null) {
        	changeRequest.setRecordType(RecordType.Division);

        	if (Helper.isDivisionExisted(pendingStandard.getDivisionId(), divisionService)) {
        		throw new IllegalArgumentException("Division with id: " + pendingStandard.getDivisionId() + " existed in system.");
        	}
        } else {
            changeRequest.setRecordType(RecordType.Org);

            if (organizationRepository.findByName(pendingStandard.getName()) != null) {
            	throw new IllegalArgumentException("Organization with name: " + pendingStandard.getName() + " existed in system.");
            }
        }
        changeRequest.setRequestedUser(Helper.getAuthUser());
        changeRequest.setRequestedDate(new Date());
        changeRequest.setPendingStandard(pendingStandard);

        ChangeRequest entity = changeRequestService.create(changeRequest);
        if (standard.getOrganization() != null) {
            StandardDivision sd = standardDivisionService.get(standard.getNewDivisionId());
            // send create standard email.
            emailService.sendStandardMutatedEmail(OperationType.Insert, sd, pendingStandard, Helper.getAuthUser());
        } else {
        	emailService.sendDivisionOrOrgMutatedEmail(OperationType.Insert, pendingStandard, Helper.getAuthUser());
        }
        return entity;
    }
    /**
     * This method is used to handle the update operation.
     *
     * @param standardId the id of the standard for operation.
     * @param standard the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public ChangeRequest processUpdateRequest(long standardId, PendingStandard standard) throws ThorEarlyException {
    	// create the change request
    	ChangeRequest changeRequest = new ChangeRequest();
    	changeRequest.setType(OperationType.Update);
    	if (standard.getOrganization() != null) {
    		// update standard division
    		// ensure the standard exists
        	StandardDivision eStandard = standardDivisionService.get(standardId);

            standard.setCreatedBy(eStandard.getStandard().getCreatedBy());
            standard.setCreatedDate(eStandard.getStandard().getCreatedDate());
            standard.setOldDivisionId(standardId);

            // make sure the name is stored in the pendingStandard table
            if (standard.getName() == null) {
                standard.setName(eStandard.getStandard().getName());
            }
            changeRequest.setStandardId(eStandard.getStandard().getId());
    	}
    	// create it to pending standard table
    	PendingStandard pendingStandard = pendingStandardService.create(standard);

        if (standard.getOrganization() != null) {
        	changeRequest.setRecordType(RecordType.Standard);
        } else {
            if (standard.getDivisionId() != null) {
                changeRequest.setRecordType(RecordType.Division);
                String[] ids = standard.getDivisionId().split(",");
                if (ids.length > 1) {
                	String oldDivisionId = ids[0];
	                String newDivisionId = ids[1];
	                if (!oldDivisionId.equals(newDivisionId) && Helper.isDivisionExisted(newDivisionId, divisionService)) {
	                	throw new IllegalArgumentException("Division with id: " + newDivisionId + " existed in system.");
	                }
                }
            } else {
                changeRequest.setRecordType(RecordType.Org);
                Organization existed = organizationRepository.findByName(pendingStandard.getName());
                if (existed != null && existed.getId() != standardId) {
                	throw new IllegalArgumentException("Organization with name: " + pendingStandard.getName() + " existed in system.");
                }
            }
        }
        changeRequest.setRequestedUser(Helper.getAuthUser());
        changeRequest.setRequestedDate(new Date());
        changeRequest.setPendingStandard(pendingStandard);

        ChangeRequest entity = changeRequestService.create(changeRequest);
        if (standard.getOrganization() != null) {
            StandardDivision sd = standardDivisionService.get(standard.getNewDivisionId());
            // send create standard email.
            emailService.sendStandardMutatedEmail(OperationType.Update, sd, pendingStandard, Helper.getAuthUser());
        } else {
        	emailService.sendDivisionOrOrgMutatedEmail(OperationType.Update, pendingStandard, Helper.getAuthUser());
        }
        return entity;
    }

    /**
     * This method is used to handle the update operation.
     *
     * @param standardId the id of the standard for operation.
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public ChangeRequest processDeleteRequest(String standardId, String type) throws ThorEarlyException {
    	// create the change request
        ChangeRequest changeRequest = new ChangeRequest();
        if ("standard".equals(type)) {
        	// ensure the standard exists
	        StandardDivision eStandard = standardDivisionService.get(Long.parseLong(standardId));
	        PendingStandard pendingStandard = copyToPendingStandard(eStandard.getStandard());
	        // create it to pending standard table
	        pendingStandard = pendingStandardService.create(pendingStandard);
	        pendingStandard.setOldDivisionId(Long.parseLong(standardId));
	        changeRequest.setPendingStandard(pendingStandard);
        	changeRequest.setRecordType(RecordType.Standard);
        	changeRequest.setStandardId(Long.parseLong(standardId));
        } else if ("division".equals(type)) {
        	Division division = divisionService.get(standardId);
        	PendingStandard pStandard = new PendingStandard();
        	pStandard.setName(division.getName());
        	pStandard.setDivisionId(standardId);
        	if (division.getParentDivision() != null) {
        		pStandard.setDescription(division.getParentDivision().getId());
        	}
        	pStandard = pendingStandardService.create(pStandard);
        	changeRequest.setPendingStandard(pStandard);
        	changeRequest.setRecordType(RecordType.Division);
        	emailService.sendDivisionOrOrgMutatedEmail(OperationType.Delete, pStandard, Helper.getAuthUser());
        } else if ("organization".equals(type)) {
        	PendingStandard pStandard = new PendingStandard();
        	Long orgId = Long.parseLong(standardId);
        	Organization org = organizationRepository.getOne(orgId);
        	pStandard.setName(org.getName());
        	pStandard = pendingStandardService.create(pStandard);
        	changeRequest.setPendingStandard(pStandard);
        	changeRequest.setStandardId(orgId);
        	changeRequest.setRecordType(RecordType.Org);
        	emailService.sendDivisionOrOrgMutatedEmail(OperationType.Delete, pStandard, Helper.getAuthUser());
        } else {
        	throw new IllegalArgumentException("Unsupported type.");
        }
        changeRequest.setType(OperationType.Delete);
        changeRequest.setRequestedUser(Helper.getAuthUser());
        changeRequest.setRequestedDate(new Date());
        
        return changeRequestService.create(changeRequest);

    }

    private PendingStandard copyToPendingStandard(Standard standard) {
        PendingStandard pendingStandard = new PendingStandard();
        pendingStandard.setCreatedDate(standard.getCreatedDate());
        pendingStandard.setCreatedBy(standard.getCreatedBy());
        pendingStandard.setDate(standard.getDate());
        pendingStandard.setDescription(standard.getDescription());
        pendingStandard.setEdition(standard.getEdition());
        pendingStandard.setName(standard.getName());
        pendingStandard.setOrganization(standard.getOrganization());
        return pendingStandard;
    }
}

