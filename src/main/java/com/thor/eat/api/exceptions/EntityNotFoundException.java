/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.exceptions;

/**
 * This is exception is thrown if there is no entity with given id.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class EntityNotFoundException extends ThorEarlyException {

    /**
     * <p>
     * This is the constructor of <code>EntityNotFoundException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>EntityNotFoundException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

