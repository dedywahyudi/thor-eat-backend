/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.repositories;

import com.thor.eat.api.entities.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The product line repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface ProductLineRepository extends JpaRepository<ProductLine, Long>, JpaSpecificationExecutor<ProductLine> {
}

