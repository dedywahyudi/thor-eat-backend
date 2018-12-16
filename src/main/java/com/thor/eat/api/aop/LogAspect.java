/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api.aop;


import com.thor.eat.api.utils.Helper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The aop log aspect.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Aspect
@Component
public class LogAspect {
    /**
     * The logging level variables
     */
    @Value("${logging.level.com.thor.eat.api}")
    private String loggingLevel;

    /**
     * The logger pointcut.
     */
    private static final String LOG_POINTCUT = "execution(* com.thor.eat.api.services"
            + ".*.*(..)) || execution(* com.thor.eat.api.controllers.*.*(..))";

    /**
     * The logger with package name.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("com.thor.eat.api");

    /**
     * Log method entrance.
     *
     * @param joinPoint the joint point
     */
    @Before(LOG_POINTCUT)
    public void logMethodEntrance(JoinPoint joinPoint) {
        if (this.loggingLevel.equalsIgnoreCase("ERROR")) {
            String[] parameterNames = ((MethodSignature) (joinPoint.getSignature())).getParameterNames();
            if (parameterNames == null) {
                parameterNames = new String[joinPoint.getArgs().length];
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    parameterNames[i] = "Argument " + i;
                }
            }
            Helper.logEntrance(LOGGER, joinPoint.getSignature()
                    .toString(), parameterNames, joinPoint.getArgs());
        }
    }

    /**
     * Log method exit.
     *
     * @param joinPoint the join point
     * @param result    the result
     */
    @AfterReturning(pointcut = LOG_POINTCUT, returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        if (this.loggingLevel.equalsIgnoreCase("ERROR")) {
            Helper.logExit(LOGGER, joinPoint.getSignature()
                    .toString(), result);
        }
    }

    /**
     * Log exception.
     *
     * @param joinPoint the joint point
     * @param ex        the exception
     */
    @AfterThrowing(pointcut = LOG_POINTCUT, throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        if (this.loggingLevel.equalsIgnoreCase("ERROR")) {
            Helper.logException(LOGGER, joinPoint.getSignature()
                    .toString(), ex);
        }
    }
}
