package com.risen.phoenix.util.common.table;

import com.google.common.base.CaseFormat;

/**
 * Phoenix字段类型控制
 */
public class PhoenixField {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 是否为空，若为主键，必然非空
     */
    private boolean canNull;

    /**
     * 是否含有主键
     */
    private boolean primaryKey;

    public PhoenixField() {
    }

    public PhoenixField(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public PhoenixField(String fieldName, String fieldType, boolean canNull, boolean primaryKey) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.canNull = canNull;
        this.primaryKey = primaryKey;
    }

    public PhoenixField(String fieldName, String fieldType, boolean primaryKey) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.primaryKey = primaryKey;
    }

    public String getFieldName() {
        return toLowerCamel(fieldName).toUpperCase();
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getCanNull() {
        if (primaryKey){
            return true;
        }
        return canNull;
    }

    public void setCanNull(Boolean canNull) {
        this.canNull = canNull;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * aaaBbbCcc变为AAA_BBB_CCC
     */
    private String toLowerCamel(String fieldName){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }
}
