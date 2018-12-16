/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.DivisionSearchCriteria;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query division by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class DivisionSpecification implements Specification<Division> {
    /**
     * The criteria. Final.
     */
    private final DivisionSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<Division> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getRegion(), pd, root.get("region"), cb);
        String keywords = criteria.getKeyword();
        if (keywords != null) {
            List<Predicate> ors = new ArrayList<>();
            List<String> keywordList = Arrays.asList(keywords.split(","));
            for(String keyword: keywordList) {
                keyword = keyword.trim();
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("name"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("region"), cb));
            }
            pd = cb.and(pd, cb.or(ors.toArray(new Predicate[0])));
        }
        return cb.and(pd, cb.isNull(root.get("parentDivision")));
    }
}

