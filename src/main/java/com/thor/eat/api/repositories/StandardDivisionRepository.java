/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.repositories;

import com.thor.eat.api.entities.StandardDivision;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The standard division repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface StandardDivisionRepository extends JpaRepository<StandardDivision, Long>, JpaSpecificationExecutor<StandardDivision> {
	/**
	 * Get standard division by division id.
	 * @param id the division id.
	 * @return the standard divisions.
	 */
	@Query("SELECT sd FROM StandardDivision sd where isApproved=true and divisionId=?")
	List<StandardDivision> findByDivision(String id);
}

