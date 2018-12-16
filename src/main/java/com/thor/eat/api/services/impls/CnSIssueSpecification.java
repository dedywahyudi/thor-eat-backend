/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.CnSIssue;
import com.thor.eat.api.entities.CnSIssueSearchCriteria;
import com.thor.eat.api.entities.RoleType;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query cns issue by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class CnSIssueSpecification implements Specification<CnSIssue> {
    /**
     * The criteria. Final.
     */
    private final CnSIssueSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<CnSIssue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        if (Helper.getAuthUser().getRole().getName().equals(RoleType.DataManager) ||
                (criteria.getCreatedByMe() != null && criteria.getCreatedByMe())) {
            pd = Helper.buildEqualPredicate(Helper.getAuthUser().getUsername(), pd, root.get("createdBy"), cb);
        }
        pd = Helper.buildEqualPredicate(criteria.getDivisionId(), pd, root.get("divisionId"), cb);
        pd = Helper.buildEqualPredicate(criteria.getRegion(), pd, root.get("region"), cb);
        pd = Helper.buildEqualPredicate(criteria.getPriority(), pd, root.get("priority"), cb);
        String keyword = criteria.getKeyword();
        if (keyword != null) {
            pd = cb.and(pd, cb.or(
                    Helper.buildLike(keyword, root.get("description"), cb),
                    Helper.buildLike(keyword, root.get("actionPlan"), cb)
            ));
        }
        return pd;
    }
}

