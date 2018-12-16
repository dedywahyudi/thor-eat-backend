/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services;


import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.UserSearchCriteria;
import com.thor.eat.api.entities.responses.LoginResponse;
import com.thor.eat.api.exceptions.ThorEarlyException;

/**
 * The user service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserService extends GenericService<User, UserSearchCriteria> {
    LoginResponse login(String username, String password) throws ThorEarlyException;
}

