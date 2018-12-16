/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.repositories;

import com.thor.eat.api.entities.ProductLineContact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The product line contact repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface ProductLineContactRepository extends JpaRepository<ProductLineContact, Long>, JpaSpecificationExecutor<ProductLineContact> {
	List<ProductLineContact> findAllByProductLine_Id(Long id);

	List<ProductLineContact> findAllByContactEmail(String email);
}

