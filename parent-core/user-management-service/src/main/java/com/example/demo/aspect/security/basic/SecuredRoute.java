package com.example.demo.aspect.security.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
* Put it on a controller-level method endpoint
* and access verification logic will be automatically
* applied for it
* */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecuredRoute {
}
