/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thor.eat.api.entities.Paging;
import com.thor.eat.api.entities.SearchResult;
import com.thor.eat.api.entities.SortDirection;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.SearchGenericService;
import com.thor.eat.api.utils.Helper;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

/**
 * This is a base class for services that provides basic search capabilities.
 *
 * @param <T> the identifiable entity
 * @param <S> the search criteria
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class SearchBaseService<T, S> implements SearchGenericService<T, S> {
    /**
     * The default sort column by id.
     */
    public static final String ID = "id";

    /**
     * The specification executor. Should be non-null after injection.
     */
    @Autowired
    private JpaSpecificationExecutor<T> specificationExecutor;

    /**
     * Represents the default page size.
     */
    @Value("${paging.default.pagesize}")
    private int defaultPageSize;

    /**
     * Represents the object mapper for conversion.
     */
    private ObjectMapper oMapper = new ObjectMapper();

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(specificationExecutor, "specificationExecutor");
        Helper.checkConfigPositive(defaultPageSize, "defaultPageSize");
    }


    /**
     * This method is used to search for results by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws ThorEarlyException if any other error occurred during operation
     */
    public SearchResult<T> search(S criteria, Paging paging) throws ThorEarlyException {
        if (paging.getLimit() == 0) {
            paging.setLimit(defaultPageSize);
        }
        Pageable page = remapPaging(paging);

        return remapResult(criteria, paging, specificationExecutor.findAll(getSpecification(criteria), page));
    }

    /**
     * This method is used to search for results by criteria.
     *
     * @param criteria the search criteria
     * @return the search result
     * @throws ThorEarlyException if any other error occurred during operation
     */
    public List<T> search(S criteria) throws ThorEarlyException {
        return specificationExecutor.findAll(getSpecification(criteria));
    }


    /**
     * This method is used to get the Specification<T>.
     *
     * @param criteria the criteria
     * @return the specification
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws ThorEarlyException if any other error occurred during operation
     */
    protected abstract Specification<T> getSpecification(S criteria) throws ThorEarlyException;


    /**
     * This method is used to to remap paging options to spring pageable data.
     *
     * @param paging the paging options
     * @return the spring pageable data
     */
    private Pageable remapPaging(Paging paging) {
        if (paging == null || (paging.getOffset() == 0 && paging.getLimit() == 0)) {
            return null;
        }

        int pageNumber = paging.getOffset() / paging.getLimit();
        int pageSize = paging.getLimit();

        Sort.Direction direction = remapSortDirection(paging);
        if (paging.getSortColumn() != null) {
            String[] properties = paging.getSortColumn().split(",");
            List<Sort.Order> orders = new ArrayList<>();
            for (String property : properties) {
                String[] fields = property.split(" ");
                String columnName = fields[0];
                if (fields.length >= 2) {
                    direction = SortDirection.valueOf(fields[1]) == SortDirection.desc ?
                            Sort.Direction.DESC : Sort.Direction.ASC;
                }
                orders.add(new Sort.Order(direction, columnName));
            }
            return new PageRequest(pageNumber, pageSize, new Sort(orders));
        }

        return new PageRequest(pageNumber, pageSize, direction, ID);
    }

    /**
     * This method is used to to remap paging options to spring sort data direction.
     *
     * @param paging the paging options
     * @return the spring sort data direction
     */
    private Sort.Direction remapSortDirection(Paging paging) {
        return paging.getSortDirection() == SortDirection.desc ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    /**
     * This method is used to to remap spring page data to SearchResult.
     *
     *
     * @param paging the paging information
     * @param pageResult the spring page result
     * @return the search result
     */
    @SuppressWarnings("unchecked")
    private SearchResult<T> remapResult(S criteria, Paging paging, Page<T> pageResult) {
        SearchResult<T> result = new SearchResult<>();
        result.setItems(pageResult.getContent());
        result.setTotal(pageResult.getTotalElements());

        // set the query
        Map<String, Object> query = new HashMap<>();
        Map<String, Object> pagingMap = this.oMapper.convertValue(paging, Map.class);
        Map<String, Object> criteriaMap = this.oMapper.convertValue(criteria, Map.class);

        query.putAll(pagingMap);
        query.putAll(criteriaMap);
        query.values().removeIf(Objects::isNull);
        result.setQuery(query);
        return result;
    }


    /**
     * Copies the properites.
     * @param source the source.
     * @param target the target.
     * @throws ThorEarlyException if there are any errors
     */
    protected void copyProperties(Object source, Object target) throws ThorEarlyException {
        try {
            PropertyUtils.describe(source).entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .filter(e -> ! e.getKey().equals("class"))
                    .forEach(e -> {
                        try {

                            PropertyUtils.setProperty(target, e.getKey(), e.getValue());
                        } catch (Exception ex) {
                            // ignore
                        }
                    });
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ThorEarlyException("Failed to copy the properties.", e);
        }
    }

}

