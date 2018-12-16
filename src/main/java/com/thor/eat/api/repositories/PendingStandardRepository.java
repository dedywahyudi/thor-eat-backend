/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.repositories;

import com.thor.eat.api.entities.PendingStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The pending standard repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface PendingStandardRepository extends JpaRepository<PendingStandard, Long>, JpaSpecificationExecutor<PendingStandard> {
}

