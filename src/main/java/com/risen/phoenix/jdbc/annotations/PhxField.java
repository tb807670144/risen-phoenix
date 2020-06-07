package com.risen.phoenix.jdbc.annotations;

import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhxField {
    PhxDataTypeEnum value();
    int length() default 0;
}
