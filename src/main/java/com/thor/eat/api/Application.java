/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */

package com.thor.eat.api;


import com.thor.eat.api.utils.Helper;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * The main application.
 *
 * <p>Changes in version 1.1 (FAST! Eaton - Thor Early Alert Tool WAR File in JBOSS Server External setting properties)
 * <ul>
 * <li>Enable application to load external configuration.</li>
 * </ul>
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.thor.eat.api")
public class Application extends SpringBootServletInitializer {
    /**
     * The request id listener.
     */
    public class RequestIdListener implements ServletRequestListener {
        /**
         * Handle request initialized event.
         *
         * @param event the servlet request event.
         */
        public void requestInitialized(ServletRequestEvent event) {
            MDC.put("RequestId", UUID.randomUUID().toString());
        }

        /**
         * Handle request destroyed event.
         *
         * @param event the servlet request event.
         */
        public void requestDestroyed(ServletRequestEvent event) {
            MDC.clear();
        }
    }

    /**
     * Create request id listener bean.
     *
     * @return the request id listener bean.
     */
    @Bean
    public RequestIdListener executorListener() {
        return new RequestIdListener();
    }

    /**
     * Custom dispatcher servlet bean.
     *
     * @return the dispatcher servlet bean.
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        // handle 404 error
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

    /**
     * The error attributes.
     *
     * @return the custom error attributes.
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            /**
             * Format error attributes with code and message key only.
             * @param requestAttributes the request attributes.
             * @param includeStackTrace the include stack trace flag.
             * @return the error attributes with code and message key only.
             */
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean
                    includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                if (!errorAttributes.containsKey("code") || !errorAttributes.containsKey("message")
                        || errorAttributes.size() != 2) {
                    Throwable error = getError(requestAttributes);
                    Object status = errorAttributes.getOrDefault("status",
                            HttpStatus.INTERNAL_SERVER_ERROR.value());
                    Object message = errorAttributes.getOrDefault("message",
                            error != null && !Helper.isNullOrEmpty(error.getMessage()) ? error.getMessage()
                                    : "Unexpected error");
                    errorAttributes.clear();
                    errorAttributes.put("code", status);
                    errorAttributes.put("message", message);
                }
                return errorAttributes;
            }
        };
    }


    /**
     * The email engine for body.
     * @return the engine.
     */
    @Bean(name = "bodyTemplateEngine")
    public TemplateEngine bodyTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver("body", TemplateMode.HTML));
        return templateEngine;
    }

    /**
     * The email engine for subject.
     * @return the engine.
     */
    @Bean(name = "subjectTemplateEngine")
    public TemplateEngine subjectTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver("subject", TemplateMode.TEXT));
        return templateEngine;
    }

    /**
     * The template resolver.
     * @param file the file.
     * @param mode the mode.
     * @return the resolver.
     */
    private ITemplateResolver templateResolver(String file, TemplateMode mode) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        String suffix = ".html";
        if (mode == TemplateMode.TEXT) {
            suffix = ".txt";
        }
        templateResolver.setSuffix("/" + file + suffix);
        templateResolver.setTemplateMode(mode);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /**
     * Configure the application.
     * @param builder the application builder.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
       return configureApplication(builder);
    }

    /**
     * Override the spring config location to enable external configuration.
     *
     * @return Spring configuration properties
     */
    private static Properties getProperties() {
         Properties props = new Properties();
         props.put("spring.config.location", "classpath:.");
         return props;
    }

    /**
     * Configure the application.
     * @param builder the application builder.
     */
    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(Application.class).properties(getProperties());
    }

    /**
     * The main entry point of the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }
}
