/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.repositories;

import com.thor.eat.api.entities.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The role repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long>,
        JpaSpecificationExecutor<PasswordHistory> {

    List<PasswordHistory> findByUserId(long userId);
    List<PasswordHistory> removeByUserId(long userId);
}

