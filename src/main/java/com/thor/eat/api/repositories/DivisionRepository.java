/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.repositories;

import com.thor.eat.api.entities.Division;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The division repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface DivisionRepository extends JpaRepository<Division, String>, JpaSpecificationExecutor<Division> {
    List<Division> findByParentDivisionId(String parentDivisionId);
}

