package com.urwoo.framework.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Description {

    @AliasFor(value = "des")
    String value() default "";

    @AliasFor(value = "value")
    String des() default "";
}
