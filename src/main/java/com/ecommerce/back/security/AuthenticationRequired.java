package com.ecommerce.back.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作为Controller类的方法注解，注明了访问该方法所需的权限
 * AuthenticationLevel[i] 和 specifics[i] 代表：
 * specifics[i]为true时，需要AuthenticationLevel[i]的特定用户的权限，因此在请求参数中要带上JWTUtil.SPECIFIC_PARAM_NAME来指定特定用户名
 * specifics[i]为false时，只需要访问者具有AuthenticationLevel[i]的权限
 * <pre>
 *     例如：
 *     <code>@AuthenticationRequired(levels = {AuthenticationLevel.USER}, specifics = {false})</code>
 *     代表该方法可以被具有USER权限的任意人访问
 *     <code>@AuthenticationRequired(levels = {AuthenticationLevel.USER,AuthenticationLevel.ADMIN}, specifics = {true,false})</code>
 *     代表该方法可以被具有USER权限的特定用户及具有ADMIN权限的任意人访问
 * </pre>
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
     * true then request with RequestParam JWTUtil.SPECIFIC_PARAM_NAME must be the same with "Name" in jwt carried in RequestHeader
     */
    boolean[] specifics() default {false};
}
