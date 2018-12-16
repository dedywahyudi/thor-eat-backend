/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.exceptions;

/**
 * The configuration exception for the application. Thrown if there is an error during in configuration.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ConfigurationException extends RuntimeException {

    /**
     * <p>
     * This is the constructor of <code>ConfigurationException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>ConfigurationException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

