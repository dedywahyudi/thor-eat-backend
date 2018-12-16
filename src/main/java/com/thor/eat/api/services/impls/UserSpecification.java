/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;

import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.UserSearchCriteria;
import com.thor.eat.api.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query user by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class UserSpecification implements Specification<User> {
    /**
     * The criteria. Final.
     */
    private final UserSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form
     * of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        String keyword = criteria.getKeyword();
        if (keyword != null) {
            pd = cb.and(pd, cb.or(Helper.buildLike(keyword, root.get("username"), cb),
                    Helper.buildLike(keyword, root.get("email"), cb),
                    Helper.buildLike(keyword, root.get("fullName"), cb)));
        }
        if (criteria.getRoleTypes() != null && !criteria.getRoleTypes().isEmpty()) {
            pd = cb.and(pd, root.get("role").get("name").in(criteria.getRoleTypes()));
        }
        return pd;
    }
}

