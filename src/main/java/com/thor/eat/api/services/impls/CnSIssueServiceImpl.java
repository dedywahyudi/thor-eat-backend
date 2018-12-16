/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.CnSIssueSearchCriteria;
import com.thor.eat.api.entities.History;
import com.thor.eat.api.entities.OperationType;
import com.thor.eat.api.entities.RecordType;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.DivisionRepository;
import com.thor.eat.api.repositories.StandardRepository;
import com.thor.eat.api.services.CnSIssueService;
import com.thor.eat.api.services.EmailService;
import com.thor.eat.api.services.HistoryService;
import com.thor.eat.api.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * The Spring Data JPA implementation of CnSIssueService,
 * extends BaseService<CnSIssue, CnSIssueSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class CnSIssueServiceImpl
        extends BaseService<CnSIssue, CnSIssueSearchCriteria> implements CnSIssueService {

    /**
     * Represents the standard repository
     */
    @Autowired
    private StandardRepository standardRepository;

    /**
     * Represents the division repository
     */
    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private HistoryService historyService;

    /**
     * Represents the email service.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @Override
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(standardRepository, "standardRepository");
        Helper.checkConfigNotNull(divisionRepository, "divisionRepository");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<CnSIssue> getSpecification(CnSIssueSearchCriteria criteria)
            throws ThorEarlyException {
        return new CnSIssueSpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    protected void interceptCreateOrUpdate(CnSIssue entity, CnSIssue existing) throws ThorEarlyException {
        super.interceptCreateOrUpdate(entity, existing);
        if (existing == null) {
            // for create
            entity.setCreatedDate(new Date());
            entity.setCreatedBy(Helper.getAuthUser().getUsername());
            entity.setRead(false);
        } else {
            if (entity.getCreatedBy() == null) {
                entity.setCreatedBy(existing.getCreatedBy());
            }
            entity.setCreatedDate(existing.getCreatedDate());
        }
        validateIdReference(entity.getStandardId(), "standardId", standardRepository, true);
        validateDivisionIdReference(entity.getDivisionId(), "divisionId", divisionRepository, existing != null);
    }

    @Override
    public CnSIssue create(CnSIssue entity) throws ThorEarlyException {
        CnSIssue result = super.create(entity);
        // create an history
        createHistory(result, OperationType.Insert);

        //send email
        emailService.sendCnSIssueMutatedEmail(OperationType.Insert, result, Helper.getAuthUser());
        return result;
    }

    @Override
    public CnSIssue update(long id, CnSIssue entity) throws ThorEarlyException {
        CnSIssue result = super.update(id, entity);
        createHistory(result, OperationType.Update);
        return result;
    }

    @Override
    public void delete(long id) throws ThorEarlyException {
        CnSIssue result = get(id);
        super.delete(id);
        createHistory(result, OperationType.Delete);
    }

    @Transactional
    @Override
    public CnSIssue markRead(long id) throws ThorEarlyException {
        CnSIssue result = get(id);
        result.setRead(true);
        return getRepository().save(result);
    }

    /**
     * This method is fill division name.
     *
     * @param entity the CnSIssue entity
     * @throws ThorEarlyException if any error occurred during operation
     */
    @Override
    public void fillDivisionName(CnSIssue entity) {
        entity.setDivision(divisionRepository.getOne(entity.getDivisionId()).getName());
    }

    private void createHistory(CnSIssue cnSIssue, OperationType operationType) throws ThorEarlyException {
        History history = new History();
        history.setRecordId(cnSIssue.getId());
        history.setUser(Helper.getAuthUser());
        history.setModifiedDate(new Date());
        history.setOperation(operationType);
        history.setRecordType(RecordType.CnSIssue);
        history.setRecordName(cnSIssue.getDescription());
        historyService.create(history);
    }
}

