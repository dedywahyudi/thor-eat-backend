/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.thor.eat.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;
import java.io.IOException;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

/**
 * The application security config.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The stateless auth filter.
     */
    @Autowired
    private StatelessAuthenticationFilter statelessAuthenticationFilter;

    /**
     * Ignore security check for static files.
     * @param web the web
     * @throws Exception if there are any errors
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/a/**", "/assets/**", "/");
    }

    /**
     * Configure web MVC.
     * 
     * @return the web MVC configurer
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }
            
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/**/*")
                .addResourceLocations("/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath,
                        Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                        : new ClassPathResource("/index.html");
                    }
                });
            } 
        };
  }

    /**
     * Configure authentication.
     *
     * @param http the http
     * @throws Exception if there is any error
     */
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(statelessAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .exceptionHandling()
            .and()
            .anonymous()
            .and()
            .servletApi()
            .and()
            .headers()
            .cacheControl()
            .and()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/login").permitAll()

            .antMatchers("/a/**").permitAll() // context path for all angular page.
            .antMatchers("/assets/**").permitAll() // for all assets
            .antMatchers(HttpMethod.GET, "/**").permitAll() // for all js and css files.

            .antMatchers("/users/**").hasAnyRole("Administrator", "AdminApprover")

            .antMatchers(POST, "/organizations/**").hasAnyRole("Administrator")
            .antMatchers(PUT, "/organizations/**").hasAnyRole("Administrator")
            .antMatchers(DELETE, "/organizations/**").hasAnyRole("Administrator")
            .antMatchers(GET, "/organizations/**").permitAll()

            .antMatchers(POST, "/divisions/**").hasAnyRole("Administrator", "AdminApprover", "DataManager")
            .antMatchers(PUT, "/divisions/**").hasAnyRole("Administrator", "AdminApprover", "DataManager")
            .antMatchers(DELETE, "/divisions/**").hasAnyRole("Administrator", "AdminApprover", "DataManager")
            .antMatchers(GET, "/divisions/**").permitAll()

            .antMatchers(POST, "/subDivisions/**").hasAnyRole("Administrator")
            .antMatchers(PUT, "/subDivisions/**").hasAnyRole("Administrator")
            .antMatchers(DELETE, "/subDivisions/**").hasAnyRole("Administrator")
            .antMatchers(GET, "/subDivisions/**").permitAll()

            .antMatchers(POST, "/histories/**").hasAnyRole("Administrator")
            .antMatchers(PUT, "/histories/**").hasAnyRole("Administrator")
            .antMatchers(DELETE, "/histories/**").hasAnyRole("Administrator")
            .antMatchers(GET, "/histories/**").permitAll()

            .antMatchers(POST, "/productLines/**").hasAnyRole("Administrator")
            .antMatchers(PUT, "/productLines/**").hasAnyRole("Administrator")
            .antMatchers(DELETE, "/productLines/**").hasAnyRole("Administrator")
            .antMatchers(GET, "/productLines/**").permitAll()

            .antMatchers(POST, "/productLineContacts/**").hasAnyRole("Administrator")
            .antMatchers(PUT, "/productLineContacts/**").hasAnyRole("Administrator")
            .antMatchers(DELETE, "/productLineContacts/**").hasAnyRole("Administrator")
            .antMatchers(GET, "/productLineContacts/**").permitAll()

            .antMatchers(POST, "/standards/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(PUT, "/standards/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(DELETE, "/standards/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(GET, "/standards/**").permitAll()

            .antMatchers(POST, "/standardDivisions/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(PUT, "/standardDivisions/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(DELETE, "/standardDivisions/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(GET, "/standardDivisions/**").permitAll()

            .antMatchers(POST, "/standardDivisionContacts/**").hasAnyRole("Administrator", "DataManager")
            .antMatchers(PUT, "/standardDivisionContacts/**").hasAnyRole("Administrator", "DataManager")
            .antMatchers(DELETE, "/standardDivisionContacts/**").hasAnyRole("Administrator", "DataManager")
            .antMatchers(GET, "/standardDivisionContacts/**").permitAll()

            .antMatchers(GET,"/changeRequests/**").hasRole("AdminApprover")

            .antMatchers(POST, "/cnSIssues/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(PUT, "/cnSIssues/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(DELETE, "/cnSIssues/**").hasAnyRole("Administrator", "DataManager", "AdminApprover")
            .antMatchers(GET,"/cnSIssues/**").authenticated()

            .anyRequest()
            .authenticated();
    }
}

