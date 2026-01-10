package com.webEng.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

@Configuration
/**
 * Configuration for enabling parameter validation.
 * (https://www.baeldung.com/spring-validate-requestparam-pathvariable)
 */
public class ClientWebConfig implements WebMvcConfigurer {

    /**
     * Parameter validation method.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * Config so Spring doesn't catch invalid and undefined content-type. We handle
     * this in ContentTypeNegotiator
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .ignoreAcceptHeader(true)
                .favorParameter(false)
                .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .mediaType("json", org.springframework.http.MediaType.APPLICATION_JSON)
                .mediaType("csv", org.springframework.http.MediaType.valueOf("text/csv"));
    }

}