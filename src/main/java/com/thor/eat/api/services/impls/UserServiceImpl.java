/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package  com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.PasswordHistory;
import com.thor.eat.api.entities.Role;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.entities.UserSearchCriteria;
import com.thor.eat.api.entities.responses.LoginResponse;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.PasswordHistoryRepository;
import com.thor.eat.api.repositories.RoleRepository;
import com.thor.eat.api.repositories.UserRepository;
import com.thor.eat.api.services.UserService;
import com.thor.eat.api.utils.Helper;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User, UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserServiceImpl
        extends BaseService<User, UserSearchCriteria> implements UserService {


    /**
     * Represents the repository for password history.
     */
    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    /**
     * Represents the user repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Represents the role repository.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(passwordHistoryRepository, "passwordHistoryRepository");
        Helper.checkConfigNotNull(userRepository, "userRepository");
        Helper.checkConfigNotNull(roleRepository, "roleRepository");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    protected Specification<User> getSpecification(UserSearchCriteria criteria)
            throws ThorEarlyException {
        return new UserSpecification(criteria);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    public User create(User entity) throws ThorEarlyException {
        checkPasswordFormat(entity.getPassword());
        validateReference(entity, "role", roleRepository, Role.class, false);
        User eUser = userRepository.findOneByUsername(entity.getUsername());
        if (eUser != null) {
            throw new IllegalArgumentException("username: " + entity.getUsername() + " already exists.");
        }

        eUser = userRepository.findOneByEmail(entity.getEmail());
        if (eUser != null) {
            throw new IllegalArgumentException("email: " + entity.getEmail() + " already exists.");
        }

        User user = super.create(entity);
        // create the password history
        PasswordHistory passwordHistory = new PasswordHistory();
        passwordHistory.setPassword(Helper.getPasswordEncoder().encode(entity.getPassword()));
        passwordHistory.setCreatedDate(new Date());
        passwordHistory.setUserId(user.getId());
        passwordHistoryRepository.save(passwordHistory);
        return user;
    }

    /**
     * This method is used to delete an user.
     *
     * @param id the id of the user to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public void delete(long id) throws ThorEarlyException {
        passwordHistoryRepository.removeByUserId(id);
        super.delete(id);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Override
    public User update(long id, User entity) throws ThorEarlyException {
        validateReference(entity, "role", roleRepository, Role.class, true);

        User user = super.update(id, entity);

        if (entity.getPassword() != null) {
            // check the password format
            checkPasswordFormat(entity.getPassword());

            // check the password history

            List<PasswordHistory> passwords = passwordHistoryRepository.findByUserId(user.getId());
            passwords.sort((p1, p2) -> p2.getCreatedDate().compareTo(p1.getCreatedDate()));
            if (passwords.size() == 0) {
                throw new IllegalArgumentException("username or password is not correct");
            }

            for (int i = 0; i < passwords.size() && i < 24; i++) {
                PasswordHistory passwordHistory = passwords.get(i);
                // verify the password
                if (Helper.getPasswordEncoder().matches(entity.getPassword(), passwordHistory.getPassword())) {
                    throw new IllegalArgumentException("cannot change to a history password within 24 generations");
                }
            }

            // create the password history
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setPassword(Helper.getPasswordEncoder().encode(entity.getPassword()));
            passwordHistory.setCreatedDate(new Date());
            passwordHistory.setUserId(user.getId());
            passwordHistoryRepository.save(passwordHistory);
        }
        return user;
    }

    /**
     * Logins a user.
     * @param username the username.
     * @param password the password
     * @return the login response.
     * @throws ThorEarlyException
     */
    @Override
    public LoginResponse login(String username, String password) throws ThorEarlyException {
        // find the user by username
        User user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("username or password is not correct");
        }

        List<PasswordHistory> passwords = passwordHistoryRepository.findByUserId(user.getId());
        passwords.sort((p1, p2) -> p2.getCreatedDate().compareTo(p1.getCreatedDate()));
        if (password.length() == 0) {
            throw new IllegalArgumentException("username or password is not correct");
        }
        PasswordHistory passwordHistory = passwords.get(0);
        // verify the password
        if (!Helper.getPasswordEncoder().matches(password, passwordHistory.getPassword())) {
            throw new IllegalArgumentException("username or password is not correct");
        }
        // login success, create the access token
        user.setAccessToken(UUID.randomUUID().toString());
        user.setAccessTokenExpiresDate(new Date(new Date().getTime() + 86400 * 1000));

        userRepository.save(user);

        // convert the user info to login response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessTokenExpiresDate(user.getAccessTokenExpiresDate());
        loginResponse.setAccessToken(user.getAccessToken());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setFullName(user.getFullName());
        loginResponse.setRole(user.getRole());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setId(user.getId());

        return loginResponse;
    }

    /**
     * Checks the password format.
     *
     * @throws IllegalArgumentException if the password format is not valid.
     * @param password the password.
     */
    private void checkPasswordFormat(String password) {
        Helper.checkNullOrEmpty(password, "password");
        if (password.length() < 9) {
            throw new IllegalArgumentException("password should contains at least 9 characters");
        }
        CharacterRule[] rules = new CharacterRule[]{
                // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1)
        };
        int c = 0;
        PasswordData passwordData = new PasswordData(password);
        for (CharacterRule rule : rules) {
            RuleResult result = rule.validate(passwordData);
            if (result.isValid()) {
                c++;
            }
        }
        if (c < 3) {
            throw new IllegalArgumentException("password should meet 3 of the following 4 conditions: " +
                    "at least contains 1 upper case character; " +
                    "at least contains 1 lower case character; " +
                    "at least contains 1 digit; " +
                    "at least contains 1 special character.");
        }

    }
}

