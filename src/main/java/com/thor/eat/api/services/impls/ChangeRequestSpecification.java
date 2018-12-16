/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.ChangeRequest;
import com.thor.eat.api.entities.ChangeRequestSearchCriteria;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * The specification used to query change request by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class ChangeRequestSpecification implements Specification<ChangeRequest> {
    /**
     * The criteria. Final.
     */
    private final ChangeRequestSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<ChangeRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        String keyword = criteria.getKeyword();
        if (keyword != null) {
            List<Predicate> ors = new ArrayList<>();
            ors.add(Helper.buildLike(keyword, root.get("pendingStandard").get("name"), cb));
            ors.add(Helper.buildLike(keyword, root.get("pendingStandard").get("description"), cb));
            pd = cb.and(pd, cb.or(ors.toArray(new Predicate[0])));
        }
        return pd;
    }
}

