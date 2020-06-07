package com.risen.phoenix.jdbc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhxTabName {
    String tableName() default "";
    String schem() default "";
    boolean upLower() default true;
}
