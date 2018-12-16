/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.ProductLineContact;
import com.thor.eat.api.entities.StandardDivision;
import com.thor.eat.api.entities.StandardDivisionSearchCriteria;
import com.thor.eat.api.repositories.ProductLineContactRepository;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The specification used to query standard division by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class StandardDivisionSpecification implements Specification<StandardDivision> {
    /**
     * The criteria. Final.
     */
    private final StandardDivisionSearchCriteria criteria;


    /**
     * Represents the product line contact repository.
     */
    private final ProductLineContactRepository productLineContactRepository;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<StandardDivision> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getOrganizationId(), pd,
                root.get("standard").get("organization").get("id"), cb);
        pd = Helper.buildEqualPredicate(criteria.getStandardId(), pd,
                root.get("standard").get("id"), cb);
        if(criteria.getProductLine() != null){
            pd = cb.or(pd, Helper.buildLike(criteria.getProductLine(), root.get("productLine").get("name"), cb));
        }
        pd = Helper.buildEqualPredicate(criteria.getSubDivisionId(), pd,
                root.join("subDivision", JoinType.LEFT).get("id"), cb);
        pd = Helper.buildEqualPredicate(criteria.getDivisionId(), pd,
                root.get("division").get("id"), cb);
        pd = Helper.buildLikePredicate(criteria.getProductLine(), pd,
                root.join("productLine", JoinType.LEFT).get("name"), cb);
        pd = Helper.buildLikePredicate(criteria.getDescription(), pd,
        		root.get("standard").get("description"), cb);
        pd = Helper.buildEqualPredicate(criteria.getStandardName(), pd, root.get("standard").get("name"), cb);
        String email = criteria.getEmail();
        if (email != null) {
            // find the contacts
            List<ProductLineContact> contacts = productLineContactRepository.findAllByContactEmail(email);
            List<Long> ids = new ArrayList<Long>();
            for (ProductLineContact contact : contacts) {
            	ids.add(contact.getProductLine().getId());
            }
            List<Predicate> ors = new ArrayList<>();
            if (ids.size() > 0) {
                ors.add(root.get("productLine").get("id").in(ids));
            }

            ors.add(Helper.buildLike(email, root.get("rawStandardParticipant"), cb));
            pd = cb.and(pd, cb.or(ors.toArray(new Predicate[0])));
        }
        String keywords = criteria.getKeyword();
        if (keywords != null) {
            List<Predicate> ors = new ArrayList<>();
            List<String> keywordList = Arrays.asList(keywords.split(","));
            for(String keyword: keywordList) {
                keyword = keyword.trim();
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("rawStandardParticipant"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("standard").get("name"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("standard").get("description"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("standard").get("organization").get("name"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("division").get("name"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("subDivision").get("name"), cb));
                ors.add(Helper.buildPredicateFromComplexKeyword(keyword, null, root.get("productLine").get("name"), cb));
            }
            pd = cb.and(pd, cb.or(ors.toArray(new Predicate[0])));
        }
        if (criteria.getIds() != null && !criteria.getIds().isEmpty()) {
            pd = cb.and(root.get("id").in(criteria.getIds()));
        }
        if (criteria.getIsApproved() != null) {
        	pd = Helper.buildEqualPredicate(criteria.getIsApproved(), pd, root.get("isApproved"), cb);
        }
        return pd;
    }
}

