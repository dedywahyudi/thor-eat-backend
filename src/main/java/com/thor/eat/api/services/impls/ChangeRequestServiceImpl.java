/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.aop.LogAspect;
import com.thor.eat.api.entities.*;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.OrganizationRepository;
import com.thor.eat.api.services.*;
import com.thor.eat.api.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Spring Data JPA implementation of ChangeRequestService,
 * extends BaseService<ChangeRequest, ChangeRequestSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class ChangeRequestServiceImpl
        extends BaseService<ChangeRequest, ChangeRequestSearchCriteria> implements ChangeRequestService {
	/**
     * Represents the organization repository
     */
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private StandardService standardService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private PendingStandardService pendingStandardService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StandardDivisionService standardDivisionService;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<ChangeRequest> getSpecification(ChangeRequestSearchCriteria criteria)
            throws ThorEarlyException {
        return new ChangeRequestSpecification(criteria);
    }

    @Transactional
    @Override
    public List<String> approveChangeRequest(long changeRequestId) throws ThorEarlyException {
        ChangeRequest changeRequest = get(changeRequestId);
        PendingStandard pStandard = changeRequest.getPendingStandard();

        History history = new History();
        history.setUser(Helper.getAuthUser());
        List<String> emails = new ArrayList<>();
        if (changeRequest.getType() == OperationType.Insert) {
            // insert it to StandardTable
            history.setOperation(OperationType.Insert);
            history.setModifiedDate(new Date());
            history.setRecordType(changeRequest.getRecordType());

            StandardDivision sd = null;
            if(changeRequest.getRecordType() == RecordType.Standard) {
                Standard standard = copyToStandard(pStandard);

                // associated with standard division and make the status to approved.
                standard = standardService.create(standard);
                sd = standardDivisionService.get(pStandard.getNewDivisionId());
                sd.setIsApproved(true);
                sd.setStandard(standard);

                history.setRecordName(standard.getName());
                history.setRecordId(standard.getId());
            } else if(changeRequest.getRecordType() == RecordType.Division) {
            	if (Helper.isDivisionExisted(pStandard.getDivisionId(), divisionService)) {
            		throw new IllegalArgumentException("Division with id: " + pStandard.getDivisionId() + " existed in system.");
            	}
                Division division = new Division();
                division.setId(pStandard.getDivisionId());
                division.setName(pStandard.getName());
                division.setRegion(pStandard.getEdition());
                if (pStandard.getDescription() != null) {
                    division.setParentDivision(divisionService.get(pStandard.getDescription()));
                }
                division = divisionService.create(division);

                history.setRecordName(division.getName());
                history.setRecordId(1L);
            } else {
            	if (organizationRepository.findByName(pStandard.getName()) != null) {
                	throw new IllegalArgumentException("Organization with name: " + pStandard.getName() + " existed in system.");
                }
                Organization organization = new Organization();
                organization.setName(pStandard.getName());
                organization = organizationService.create(organization);

                history.setRecordName(organization.getName());
                history.setRecordId(organization.getId());
            }

            // delete the current change request
            delete(changeRequestId);

            // create the history record
            historyService.create(history);

            if (sd != null) {
                // send approve email
            	emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), sd, pStandard.getName());
            } else {
            	emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), null, pStandard.getName());
            }
            // send the email
//            emailService.sendStandardMutatedEmail(OperationType.Insert, standard, changeRequest.getRequestedUser(),
//            		changeRequest.getPendingStandard().getNewDivisionId());
        } else if (changeRequest.getType() == OperationType.Update) {
        	history.setOperation(OperationType.Update);
            history.setModifiedDate(new Date());
        	if(changeRequest.getRecordType() == RecordType.Standard) {
	            Standard standard = copyToStandard(pStandard);
	            standard = standardService.update(changeRequest.getStandardId(), standard);
	
	            // update the standard division and delete new standard division
	            StandardDivision sd = standardDivisionService.get(pStandard.getNewDivisionId());
	            sd.setIsApproved(true);
	            sd.setStandard(standard);
	
	            StandardDivision updatedEntity = standardDivisionService.update(pStandard.getOldDivisionId(), sd);

	            history.setRecordName(standard.getName());
	            history.setRecordId(standard.getId());
	            history.setRecordType(RecordType.Standard);

	            standardDivisionService.delete(pStandard.getNewDivisionId());
	         // send approve email
	            emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), updatedEntity, pStandard.getName());
        	} else if (changeRequest.getRecordType() == RecordType.Division) {
        		String divisionIdStr = pStandard.getDivisionId();
        		if (divisionIdStr != null) {
        			String[] divisionIdArray = divisionIdStr.split(",");
        			String oldDivisionId = divisionIdArray[0];
        			String newDivisionId = divisionIdArray.length > 1 ? divisionIdArray[1] : oldDivisionId;
        			if (Helper.isDivisionExisted(pStandard.getDivisionId(), divisionService)) {
                		throw new IllegalArgumentException("Division with id: " + newDivisionId + " existed in system.");
                	}
	        		Division division = divisionService.get(oldDivisionId);
	        		division.setId(newDivisionId);
	                division.setName(pStandard.getName());
	                division.setRegion(pStandard.getEdition());
	                if (pStandard.getDescription() != null) {
	                    division.setParentDivision(divisionService.get(pStandard.getDescription()));
	                }
	                if (oldDivisionId.equals(newDivisionId)) {
	                	division = divisionService.update(oldDivisionId, division);
	                } else {
	                	division = divisionService.create(division);
	                }
	                history.setRecordName(division.getName());
	                history.setRecordId(1L);
	                history.setRecordType(RecordType.Division);
	                emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), null, pStandard.getName());
        		}
        	} else {
        		Organization existed = organizationRepository.findByName(pStandard.getName());
                if (existed != null && existed.getId() != changeRequest.getStandardId()) {
                	throw new IllegalArgumentException("Organization with name: " + pStandard.getName() + " existed in system.");
                }
        		Organization org = organizationService.get(changeRequest.getStandardId());
        		org.setName(pStandard.getName());
        		org = organizationService.update(changeRequest.getStandardId(), org);
        		history.setRecordName(org.getName());
                history.setRecordId(org.getId());
                history.setRecordType(RecordType.Org);
                emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), null, pStandard.getName());
        	}
        	
            // delete the current change request
            delete(changeRequestId);

         // also delete the pending standard
            if (changeRequest.getPendingStandard() != null) {
                pendingStandardService.delete(changeRequest.getPendingStandard().getId());
            }

            // create the history record
            historyService.create(history);
//            emailService.sendStandardMutatedEmail(OperationType.Update, standard, changeRequest.getRequestedUser(),
//            		changeRequest.getPendingStandard().getOldDivisionId());
        } else if (changeRequest.getType() == OperationType.Delete) {
        	history.setOperation(OperationType.Delete);
            history.setModifiedDate(new Date());
        	if(changeRequest.getRecordType() == RecordType.Standard) {
	            StandardDivision sd = standardDivisionService.get(changeRequest.getPendingStandard().getOldDivisionId());
	
	            // delete the current change request
	            delete(changeRequestId);
	
	            standardDivisionService.delete(changeRequest.getPendingStandard().getOldDivisionId());
	            history.setRecordName(sd.getStandard().getName());
	            history.setRecordId(sd.getId());
	            history.setRecordType(RecordType.Standard);
	         // send approve email
	            emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), sd, pStandard.getName());
        	} else if (changeRequest.getRecordType() == RecordType.Division) {
        		Division division = divisionService.get(changeRequest.getPendingStandard().getDivisionId());

        		divisionService.delete(division.getId());

	            history.setRecordName(division.getName());
	            history.setRecordId(1L);
	            history.setRecordType(RecordType.Division);
	         // delete the current change request
	            delete(changeRequestId);
	            emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), null, pStandard.getName());
        	} else {
        		Organization org = organizationService.get(changeRequest.getStandardId());
        		organizationService.delete(org.getId());

	            history.setRecordName(org.getName());
	            history.setRecordId(org.getId());
	            history.setRecordType(RecordType.Org);
	         // delete the current change request
	            delete(changeRequestId);
	            emails = emailService.sendApprovedEmail(changeRequest.getRequestedUser(), null, pStandard.getName());
        	}
            // create the history record
            historyService.create(history);
        }
        return emails;
    }

    @Transactional
    @Override
    public List<String> rejectChangeRequest(long changeRequestId, String rejectMessage) throws ThorEarlyException {
        ChangeRequest changeRequest = get(changeRequestId);
        delete(changeRequestId);
        if (changeRequest.getPendingStandard() != null) {
            String standardName = changeRequest.getPendingStandard().getName();
//            Standard standard = copyToStandard(changeRequest.getPendingStandard());
            pendingStandardService.delete(changeRequest.getPendingStandard().getId());
            // get associated
            StandardDivision sd = null;
            if (changeRequest.getRecordType() == RecordType.Standard) {
                sd = standardDivisionService.get(
                        changeRequest.getType() == OperationType.Insert ?
                                changeRequest.getPendingStandard().getNewDivisionId()
                                : changeRequest.getPendingStandard().getOldDivisionId());
                
            }
            //send reject email
            return emailService.sendRejectEmail(changeRequest.getRequestedUser(), sd, standardName, rejectMessage);
//          emailService.sendStandardMutatedEmail(OperationType.Update, standard, changeRequest.getRequestedUser(),
        } else {
        	throw new ThorEarlyException("ChangeRequest's pending standard should not be null.");
        }
    }

    private Standard copyToStandard(PendingStandard pendingStandard) {
        Standard standard = new Standard();
        standard.setCreatedDate(pendingStandard.getCreatedDate());
        standard.setCreatedBy(pendingStandard.getCreatedBy());
        standard.setName(pendingStandard.getName());
        standard.setEdition(pendingStandard.getEdition());
        standard.setOrganization(pendingStandard.getOrganization());
        standard.setDescription(pendingStandard.getDescription());
        standard.setDate(pendingStandard.getDate());
        return standard;
    }
}

