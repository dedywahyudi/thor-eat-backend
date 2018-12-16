/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.exceptions;


/**
 * The base exception for the application. Thrown if there is an error during CRUD operations.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ThorEarlyException extends Exception {

    /**
     * <p>
     * This is the constructor of <code>ThorEarlyException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public ThorEarlyException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>ThorEarlyException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public ThorEarlyException(String message, Throwable cause) {
        super(message, cause);
    }
}


