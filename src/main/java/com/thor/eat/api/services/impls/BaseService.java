/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.services.impls;


import com.thor.eat.api.entities.Division;
import com.thor.eat.api.entities.IdentifiableEntity;
import com.thor.eat.api.exceptions.ConfigurationException;
import com.thor.eat.api.exceptions.EntityNotFoundException;
import com.thor.eat.api.exceptions.ThorEarlyException;
import com.thor.eat.api.services.GenericService;
import com.thor.eat.api.utils.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static lombok.AccessLevel.PROTECTED;

/**
 * This is a base class for services that provides basic CRUD capabilities.
 *
 * @param <T> the identifiable entity
 * @param <S> the search criteria
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseService<T extends IdentifiableEntity, S> extends SearchBaseService<T, S>
        implements GenericService<T, S> {

    /**
     * The repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    @Getter(value = PROTECTED)
    private JpaRepository<T, Long> repository;

    /**
     * Represents the bean utils.
     */
    private PropertyUtilsBean beanUtils = new PropertyUtilsBean();

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    public T get(long id) throws ThorEarlyException {
        return ensureEntityExist(id);
    }

    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the existing entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     * @throws EntityNotFoundException if the entity does not exist
     */
    private T checkUpdate(long id, T entity) throws EntityNotFoundException {
        Helper.checkUpdate(id, entity);
        return ensureEntityExist(id);
    }

    /**
     * Check whether an identifiable entity with a given id exists.
     *
     * @param id the id of entity
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the match entity can not be found in DB
     */
    protected T ensureEntityExist(long id) throws EntityNotFoundException {
        Helper.checkPositive(id, "id");
        T entity = repository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity not found for id: [" + id + "]");
        }
        return entity;
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public T create(T entity) throws ThorEarlyException {
        if (entity.getId() > 0) {
            throw new IllegalArgumentException("id cannot be present when creating an entity.");
        }
        Helper.checkNull(entity, "entity");

        interceptCreateOrUpdate(entity, null);

        return repository.save(entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws ThorEarlyException if any other error occurred during operation
     */
    @Transactional
    @Override
    public void delete(long id) throws ThorEarlyException {
        Helper.checkPositive(id, "id");
        ensureEntityExist(id);
        repository.delete(id);
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
    @Transactional
    @Override
    public T update(long id, T entity) throws ThorEarlyException {
        entity.setId(id);
        T existing = checkUpdate(id, entity);
        interceptCreateOrUpdate(entity, existing);
        BeanUtils.copyProperties(entity, existing, "productLine");
        return repository.save(existing);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param existing the existing entity.
     * @throws ThorEarlyException if any error occurred during operation
     */
    protected void interceptCreateOrUpdate(T entity, T existing) throws ThorEarlyException {

    }

    /**
     * Validates the reference of an entity.
     * @param entity the entity of the object.
     * @param path the path to validate.
     * @param repository the repository to access the sub-entity object.
     * @param optional is the property is optional or not.
     * @param <TT> the type of the sub-entity.
     * @return the sub-entity.
     * @throws ThorEarlyException if there are any errors.
     */
    @SuppressWarnings("unchecked")
    protected <TT extends IdentifiableEntity> TT validateReference(
            Object entity, String path, JpaRepository<TT, Long> repository, Class objectClass, boolean optional)
            throws ThorEarlyException {
        Method method;
        try {
            TT obj = (TT) beanUtils.getProperty(entity, path);
            if (!optional && obj == null) {
                throw new IllegalArgumentException(path + " is required.");
            }
            if (obj != null) {
                Helper.checkPositive(obj.getId(), path + ".id");
            }
            long id = obj != null ? obj.getId() : 0;
            if (id > 0) {
                TT persistedObj = repository.findOne(id);
                if (persistedObj == null) {
                    throw new IllegalArgumentException("The id: " + id + " of " + path + " does not exists.");
                }

                // set the persisted object to the entity
                // beanUtils.setProperty(entity, path, persistedObj);
                String methodName = "set" + path.substring(0, 1).toUpperCase() + path.substring(1);
                method = entity.getClass().getDeclaredMethod(methodName, objectClass);

                method.setAccessible(true);
                method.invoke(entity, persistedObj);

                return persistedObj;
            }

            return obj;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ThorEarlyException("cannot find object in path: " + path, e);
        } catch (ClassCastException e) {
            throw new ThorEarlyException("The class type is not correct", e);
        }
    }

    /**
     * Validates the reference of an entity.
     * @param id the id of the object.
     * @param name the name of the property.
     * @param repository the repository to access the sub-entity object.
     * @param optional is the property is optional or not.
     * @param <TT> the type of the sub-entity.
     * @return the sub-entity.
     * @throws ThorEarlyException if there are any errors.
     */
    @SuppressWarnings("unchecked")
    protected <TT extends IdentifiableEntity> TT validateIdReference(
            Long id, String name, JpaRepository<TT, Long> repository, boolean optional)
            throws ThorEarlyException {
        try {
            if (!optional && id == null) {
                throw new IllegalArgumentException(name + " is required.");
            }
            if (id != null) {
                Helper.checkPositive(id, name);
            }
            if (id != null && id > 0) {
                TT persistedObj = repository.findOne(id);
                if (persistedObj == null) {
                    throw new IllegalArgumentException("The object " + name + " does not exists.");
                }

                return persistedObj;
            }
            return null;
        } catch (ClassCastException e) {
            throw new ThorEarlyException("The class type is not correct", e);
        }
    }


    /**
     * Validates the reference of an entity.
     * @param id the id of the object.
     * @param name the name of the property.
     * @param repository the repository to access the sub-entity object.
     * @param optional is the property is optional or not.
     * @return the sub-entity.
     * @throws ThorEarlyException if there are any errors.
     */
    @SuppressWarnings("unchecked")
    protected Division validateDivisionIdReference(
            String id, String name, JpaRepository<Division, String> repository, boolean optional)
            throws ThorEarlyException {
        try {
            if (!optional && id == null) {
                throw new IllegalArgumentException(name + " is required.");
            }
            if (id != null) {
                Helper.checkNullOrEmpty(id, name);
            }
            if (id != null && !id.isEmpty()) {
                Division persistedObj = repository.findOne(id);
                if (persistedObj == null) {
                    throw new IllegalArgumentException("The object " + name + " does not exists.");
                }

                return persistedObj;
            }
            return null;
        } catch (ClassCastException e) {
            throw new ThorEarlyException("The class type is not correct", e);
        }
    }

    /**
     * Validates the reference of an entity.
     * @param entity the entity of the object.
     * @param path the path to validate.
     * @param repository the repository to access the sub-entity object.
     * @param optional is the property is optional or not.
     * @return the sub-entity.
     * @throws ThorEarlyException if there are any errors.
     */
    @SuppressWarnings("unchecked")
    protected Division validateDivisionReference(
            Object entity, String path, JpaRepository<Division, String> repository, Class objectClass, boolean optional)
            throws ThorEarlyException {
        Method method;
        try {
            Division obj = (Division) beanUtils.getProperty(entity, path);
            if (!optional && obj == null) {
                throw new IllegalArgumentException(path + " is required.");
            }
            if (obj != null) {
                Helper.checkNullOrEmpty(obj.getId(), path + ".id");
            }
            String id = obj != null ? obj.getId() : null;
            if (id != null && !id.isEmpty()) {
                Division persistedObj = repository.findOne(id);
                if (persistedObj == null) {
                    throw new IllegalArgumentException("The id: " + id + " of " + path + " does not exists.");
                }

                // set the persisted object to the entity
                // beanUtils.setProperty(entity, path, persistedObj);

                String methodName = "set" + path.substring(0, 1).toUpperCase() + path.substring(1);
                method = entity.getClass().getDeclaredMethod(methodName, objectClass);

                method.setAccessible(true);
                method.invoke(entity, persistedObj);

                return persistedObj;
            }

            return obj;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ThorEarlyException("cannot find object in path: " + path, e);
        } catch (ClassCastException e) {
            throw new ThorEarlyException("The class type is not correct", e);
        }
    }



}

