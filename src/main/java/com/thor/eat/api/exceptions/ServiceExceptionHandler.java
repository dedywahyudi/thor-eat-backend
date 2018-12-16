/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.exceptions;


import com.thor.eat.api.aop.LogAspect;
import com.thor.eat.api.utils.Helper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * The exception handler.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@EnableWebMvc
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Logging level.
     */
    @Value("${logging.level.com.thor.eat.api}")
    private String loggingLevel;


    /**
     * Handle controller exception.
     * @param exception the exception.
     * @return the error response entity.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<Object> handleControllerException(Throwable exception) {
        if (this.loggingLevel.equalsIgnoreCase("ERROR")) {
            Helper.logException(LogAspect.LOGGER, "com.thor.eat.api.exceptions"
                    + ".ServiceExceptionHandler#handleControllerException", exception);
            String stackTrace = ExceptionUtils.getStackTrace(exception);
            LogAspect.LOGGER.error(stackTrace, exception);
        }

        HttpStatus status;
        if (exception instanceof IllegalArgumentException || exception instanceof ConstraintViolationException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
        } else if (exception instanceof UnauthenticatedException) {
            status = HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (exception instanceof DataIntegrityViolationException
                && exception.getCause() instanceof ConstraintViolationException) {
            exception = new IllegalStateException(
                    "cannot process this operation, because other tables are referring this object");
            status = HttpStatus.BAD_REQUEST;
        }
        return buildErrorResponse(status, exception, this.loggingLevel);
    }

    /**
     * Handle internal exception with custom error response.
     * @param ex the exception.
     * @param headers the http header.
     * @param status the http status
     * @param request the web request.
     * @return the error response entity
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        return buildErrorResponse(status, ex, this.loggingLevel);
    }

    /**
     * Build error response.
     * @param status the http status.
     * @param ex the exception.
     * @return the error response entity with code and message.
     */
    private static ResponseEntity<Object> buildErrorResponse(HttpStatus status, Throwable ex, String logLevel) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", status.value());
        String message = !Helper.isNullOrEmpty(ex.getMessage()) ? ex.getMessage() : "Unexpected error";
        // handle message from validator
        if (logLevel.equalsIgnoreCase("ERROR")) {
            if (ex instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
                if (!e.getBindingResult().getAllErrors().isEmpty()) {
                    ObjectError err = e.getBindingResult().getAllErrors().get(0);
                    if (err instanceof FieldError) {
                        FieldError fe = (FieldError) err;
                        message = fe.getObjectName() + "." + fe.getField() + " is invalid. reason: " + fe.getDefaultMessage();
                    } else {
                        message = err.getObjectName() + " is invalid. reason: " + err.getDefaultMessage();
                    }
                }
            } else if (ex instanceof PropertyReferenceException) {
                // change to 400
                responseBody.put("code", HttpStatus.BAD_REQUEST.value());
            }
        }
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }
}
