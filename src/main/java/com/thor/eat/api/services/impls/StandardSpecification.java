/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.Standard;
import com.thor.eat.api.entities.StandardSearchCriteria;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query standard by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class StandardSpecification implements Specification<Standard> {
    /**
     * The criteria. Final.
     */
    private final StandardSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<Standard> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getOrganizationId(), pd, root.get("organization").get("id"), cb);
        String keyword = criteria.getKeyword();
        if (keyword != null) {
            pd = cb.and(pd, cb.or(
                    Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("name"), cb),
                    Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("description"), cb)
            ));
        }

        return pd;
    }

}

