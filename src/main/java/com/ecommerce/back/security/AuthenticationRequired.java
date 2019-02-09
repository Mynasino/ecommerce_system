package com.ecommerce.back.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of controller's method
 * Declared the authentication level of resources
 * Default "USER"
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationRequired {
    /**
     * all the AuthenticationLevels of resources
     */
    AuthenticationLevel[] levels() default {AuthenticationLevel.USER};
    /**
     * if specific individual is required for resource
     * true then request with RequestParam "PersonName" must be the same with "Name" in jwt carried in RequestHeader
     */
    boolean[] specifics() default {false};
}
