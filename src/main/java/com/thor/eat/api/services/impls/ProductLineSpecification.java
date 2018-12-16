/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.ProductLine;
import com.thor.eat.api.entities.ProductLineSearchCriteria;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query product line by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class ProductLineSpecification implements Specification<ProductLine> {
    /**
     * The criteria. Final.
     */
    private final ProductLineSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<ProductLine> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getDivisionId(), pd, root.get("divisionId"), cb);
        pd = Helper.buildEqualPredicate(criteria.getSubDivisionId(), pd, root.get("subDivisionId"), cb);
        return pd;
    }
}

