/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api;

import com.thor.eat.api.entities.*;
import com.thor.eat.api.repositories.RoleRepository;
import com.thor.eat.api.repositories.StandardDivisionRepository;
import com.thor.eat.api.repositories.StandardRepository;
import com.thor.eat.api.security.UserAuthentication;
import com.thor.eat.api.services.DivisionService;
import com.thor.eat.api.services.OrganizationService;
import com.thor.eat.api.services.SubDivisionService;
import com.thor.eat.api.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Date;


/**
 * This is the base class for all the unit tests.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@SpringBootTest(classes ={Application.class})
@WebAppConfiguration
@EnableWebSecurity
//@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
public abstract class BaseTests<T> {
    /**
     * The environment variables
     */
    @Autowired
    private Environment env;

	/**
	 * Represents the admin email address.
	 */
    @Value("${email.admin}")
    private String adminEmail;

    private static final String[] TABLES = new String[] {"ChangeRequest", "History", "CnSIssue",
            "StandardDivisionContact", "StandardDivision", "PendingStandard", "Standard", "ProductLineContact",
            "ProductLine", "SubDivision", "Division", "PasswordHistory", "User", "Role", "Organization"};

    @Autowired
    private UserService userService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private StandardDivisionRepository standardDivisionRepository;

    private T obj;

    private User user;

    protected Role role;
    protected Division division;

    protected Division subDivision;

    protected Organization organization;

    protected Standard standard;

    protected StandardDivision standardDivision;
    @Before
    public void setUp() throws Exception {
        // set up the auth user
        prepareData();
        Authentication authentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        obj = createTestObject(generateTestObject());
    }

    @After
    public void tearDown() throws Exception {
        clearDBData();
    }

    protected abstract T generateTestObject() throws Exception;
    protected abstract T createTestObject(T obj) throws Exception;

    protected T getTestObject() {
        return obj;
    }

    protected User getUser() {
        return user;
    }
    protected void prepareData() throws Exception {
        clearDBData();
        Role role = new Role();
        role.setName(RoleType.Administrator);
        role = roleRepository.save(role);
        this.role = role;
        User user = new User();
        user.setEmail(adminEmail);
        user.setFullName("test, account");
        user.setRole(role);
        user.setPassword(this.env.getProperty("password.admin"));
        user.setUsername("admin");
        this.user = userService.create(user);

        Division division = new Division();
        division.setId("1.1");
        division.setName("test_division");
        division.setRegion("test region");

        this.division = divisionService.create(division);

        Division subDivision = new Division();
        subDivision.setId("1.10");
        subDivision.setParentDivision(division);
        subDivision.setName("test sub division");
        subDivision.setRegion("test regison");
        this.subDivision = divisionService.create(subDivision);

        Organization organization = new Organization();
        organization.setName("test org");
        organizationService.create(organization);

        this.organization = organization;

        standard = new Standard();
        standard.setName("standard");
        standard.setOrganization(organization);
        standard.setCreatedDate(new Date());
        standard.setCreatedBy(getUser().getId());
        standard.setEdition("test-edition");
        standard.setDate(new Date());
        this.standard = standardRepository.save(standard);

        standardDivision = new StandardDivision();
        standardDivision.setDivision(this.division);
        standardDivision.setStandard(this.standard);
        standardDivision.setCriticalToBusiness(false);
        standardDivision.setIsApproved(false);
        this.standardDivision = standardDivisionRepository.save(standardDivision);

    }

    private void clearDBData() throws Exception {
        for (String table : TABLES) {
            clearTableData(table);
        }
    }

    private void clearTableData(String tableName) throws Exception {
        final String query = "DELETE FROM [" + tableName + "]";
        Query statement = em.createNativeQuery(query);
        statement.executeUpdate();
    }
}
