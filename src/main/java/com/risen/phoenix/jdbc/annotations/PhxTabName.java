package com.risen.phoenix.jdbc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhxTabName {
    /**
     * 表名称
     * @return
     */
    String tableName() default "";
    /**
     * 库名称
     * @return
     */
    String schem() default "";
    /**
     * 盐值
     * @return
     */
    int salt() default 3;
    /**
     * 是否压缩
     * @return
     */
    boolean compression() default false;
    boolean upLower() default true;
}
