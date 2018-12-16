/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.exceptions;

/**
 * This is exception is thrown if a principal does not have access to a specific entity.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnauthenticatedException extends ThorEarlyException {

    /**
     * <p>
     * This is the constructor of <code>AccessDeniedException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public UnauthenticatedException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>AccessDeniedException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

