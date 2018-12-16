/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thor.eat.api.aop.LogAspect;
import com.thor.eat.api.entities.IdentifiableEntity;
import com.thor.eat.api.entities.Organization;
import com.thor.eat.api.entities.User;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.repositories.OrganizationRepository;
import com.thor.eat.api.security.UserAuthentication;
import com.thor.eat.api.services.DivisionService;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class provides help methods used in this application.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Helper {
    /**
     * Represents the classes that there is no need to log.
     */
    private static final List<Class> NOLOGS = Arrays.asList(HttpServletRequest.class,
            HttpServletResponse.class, ModelAndView.class, MultipartFile[].class,
            ResponseEntity.class, InputStream.class);

    /**
     * The object mapper.
     */
    private static final ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();

    /**
     * The private constructor.
     */
    private Helper() { }

    /**
     * Get password encoder.
     *
     * @return the BC crypt password encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * It checks whether a given object is null.
     *
     * @param object the object to be checked
     * @param name the name of the object, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the object is null
     */
    public static void checkNull(Object object, String name) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException("[ " + name + " ] cannot be null.");
        }
    }

    /**
     * It checks whether a given identifiable entity is valid.
     *
     * @param object the object to be checked
     * @param name the name of the object, used in the exception message
     * @param <T> the entity class
     * @throws IllegalArgumentException the exception thrown when the object is null or id of object is not positive
     */
    public static <T extends IdentifiableEntity> void checkEntity(T object, String name)
            throws IllegalArgumentException {
        checkNull(object, name);
        checkPositive(object.getId(), name + ".id");
    }

    /**
     * It checks whether a given string is valid email address.
     *
     * @param str the string to be checked
     * @return true if a given string is valid email address
     */
    public static boolean isEmail(String str) {
        return new EmailValidator().isValid(str, null);
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @return true if a given string is null or empty
     * @throws IllegalArgumentException throws if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) throws IllegalArgumentException {
        return str == null || str.trim().isEmpty();
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @param name the name of the string, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the given string is null or empty
     */
    public static void checkNullOrEmpty(String str, String name) throws IllegalArgumentException {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(
                    "[" + name + "] cannot be null or empty");
        }
    }

    /**
     * Check if the value is positive.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not positive
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException("[" + name + "] must be positive.");
        }
    }


    /**
     * Check if the configuration state is true.
     *
     * @param state the state
     * @param message the error message if the state is not true
     * @throws ConfigurationException if the state is not true
     */
    public static void checkConfigState(boolean state, String message) {
        if (!state) {
            throw new ConfigurationException(message);
        }
    }

    /**
     * Check if the configuration is null or not.
     *
     * @param object the object
     * @param name the name
     * @throws ConfigurationException if the configuration is null
     */
    public static void checkConfigNotNull(Object object, String name) {
        if (object == null) {
            throw new ConfigurationException("Configuration [" + name + "] cannot be null.");
        }
    }

    /**
     * Check if the configuration is positive or not.
     *
     * @param value the configuration  value
     * @param name the name
     * @throws ConfigurationException if the configuration value is  not positive
     */
    public static void checkConfigPositive(long value, String name) {
        if (value <= 0) {
            throw new ConfigurationException("Configuration [" + name + "] must be positive.");
        }
    }

    /**
     * Logs message with <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param message the log message.
     */
    public static void logDebugMessage(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Logs for entrance into public methods at <code>ERROR</code> level.
     *
     * @param logger the logger.
     * @param signature the signature.
     * @param paramNames the names of parameters to log (not Null).
     * @param params the values of parameters to log (not Null).
     */
    public static void logEntrance(Logger logger, String signature, String[] paramNames, Object[] params) {
        if (logger.isDebugEnabled()) {
            String msg = "Entering method [" + signature + "] " + toString(paramNames, params);
            logger.debug(msg);
        }
    }

    /**
     * Logs for exit from public methods at <code>ERROR</code> level.
     *
     * @param logger the logger.
     * @param signature the signature of the method to be logged.
     * @param value the return value to log.
     */
    public static void logExit(Logger logger, String signature, Object value) {
        if (logger.isDebugEnabled()) {
            String msg = "Exiting method [" + signature + "]";
            if (value != null) {
                msg += " Output Value: " + toString(value);
            }
            logger.debug(msg);
        }
    }

    /**
     * Logs the given exception and message at <code>ERROR</code> level.
     *
     * @param <T> the exception type.
     * @param logger the logger.
     * @param signature the signature of the method to log.
     * @param ex the exception to log.
     */
    public static <T extends Throwable> void logException(Logger logger, String signature, T ex) {
        StringBuilder sb = new StringBuilder("Error occurs in [" + signature + "]");
        sb.append(": ").append(ex.getMessage());
        logger.error(sb.toString(), ex);
    }

    /**
     * Converts the parameters to string.
     *
     * @param paramNames the names of parameters.
     * @param params the values of parameters.
     * @return the string
     */
    private static String toString(String[] paramNames, Object[] params) {
        StringBuilder sb = new StringBuilder("Input Values: ");
        sb.append("{");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(paramNames[i]).append(":").append(toString(params[i]));
            }
        }
        sb.append("}.");
        return sb.toString();
    }

    /**
     * Converts the object to string.
     *
     * @param obj the object
     * @return the string representation of the object.
     */
    private static String toString(Object obj) {
        String result;
        try {
            if (NOLOGS.stream().anyMatch(s -> s.isInstance(obj))) {
                result = obj.getClass().getSimpleName();
            } else {
                result = MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            Helper.logException(LogAspect.LOGGER, "com.thor.eat.api"
                    + ".Helper#toString", e);
            
            result = "Failed to pass json: " + e.getMessage();
        }
        return result;
    }
    /**
     * Build equal predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(String val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            return cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build >= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate
    buildGreaterThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.greaterThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build <= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate
    buildLessThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.lessThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build equal predicate for object value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(Object val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLikePredicate(String val, Predicate pd, Path<String> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            return cb.and(pd, buildLike(val, path, cb));
        }
        return pd;
    }

    /**
     * Build predicate from complex keyword (with comma, OR, AND, NOT).
     *
     * @param keyword the keyword
     * @param pd the predicate
     * @param sp the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildPredicateFromComplexKeyword(String keyword, Predicate pd, Path<String> sp, CriteriaBuilder cb) {
        // Parse by AND
        String[] andKeywords = keyword.toLowerCase().split("and");
        for (String akw : andKeywords) {

            // Parse by OR
            String[] orKeywords = new String[]{};
            if (akw.contains(",")) {
                orKeywords = akw.split(",");
            } else {
                orKeywords = akw.split("or");
            }

            Predicate orPd = null;
            for (String okw : orKeywords) {
                String kw = okw.trim();
                boolean isNot = kw.startsWith("not");
                if (isNot) {
                    kw = kw.replace("not", "").trim();
                }

                Predicate p = isNot ? Helper.buildNotLike(kw, sp, cb) : Helper.buildLike(kw, sp, cb);;

                if (orPd == null) {
                    orPd = p;
                } else {
                    orPd = cb.or(orPd, p);
                }
            }

            if (pd == null) {
                pd = orPd;
            } else {
                pd = cb.and(pd, orPd);
            }
        }

        return pd;
    }

    /**
     * Build predicate to match ids in identifiable entity list.
     *
     * @param val the list value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <T> the identifiable entity
     * @return the match predicate
     */
    public static <T extends IdentifiableEntity> Predicate
    buildInPredicate(List<T> val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null && !val.isEmpty()) {
            List<Long> ids = val.stream().map(IdentifiableEntity::getId).collect(Collectors.toList());
            return cb.and(pd, path.in(ids));
        }

        return pd;
    }


    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLike(String val, Path<String> path, CriteriaBuilder cb) {
        return cb.like(path, "%" + val + "%");
    }

    /**
     * Build NOT like predicate for string value.
     *
     * @param val the value
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildNotLike(String val, Path<String> path, CriteriaBuilder cb) {
        return cb.notLike(path, "%" + val + "%");
    }

    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @param <T> the entity class
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     */
    public static <T extends IdentifiableEntity> void checkUpdate(long id, T entity) {
        checkPositive(id, "id");
        checkNull(entity, "entity");
        checkPositive(entity.getId(), "entity.id");
        if (entity.getId() != id) {
            throw new IllegalArgumentException(
                    "The id in the entity is not consistent with the id in the request.");
        }
    }

    /**
     * Get user from authentication.
     * @return user if exists valid user authentication otherwise null
     */
    public static User getAuthUser()  {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserAuthentication) {
            UserAuthentication userAuth = (UserAuthentication) auth;
            user = (User) userAuth.getPrincipal();
        }

        return user;
    }

    /**
     * Check the division existed or not.
     * @param id the division id.
     * @param divisionService the division service.
     * @return the result.
     * @throws ThorEarlyException if any error occurs.
     */
    public static boolean isDivisionExisted(String id, DivisionService divisionService) throws ThorEarlyException {
    	try {
    		divisionService.get(id);
    	} catch (EntityNotFoundException e) {
    		// valid division id.
    		return false;
    	}
    	return true;
    }
}
