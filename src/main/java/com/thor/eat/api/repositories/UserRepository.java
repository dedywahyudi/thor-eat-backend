/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.repositories;


import com.thor.eat.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * This is the repository for accessing the bearer tokens.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Finds the bearer token by the bearer token string.
     * @param accessToken the bearer access token.
     * @return the bearer token entity.
     */
    List<User> findByAccessToken(String accessToken);

    User findOneByUsername(String username);

    User findOneByEmail(String email);
}
