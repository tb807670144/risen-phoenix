package com.risen.phoenix.jdbc.core.enums;

public enum PhxDataTypeEnum {
    /**
     * INTEGER
     */
    INTEGER("INTEGER", "Integer"),
    UNSIGNED_INT("UNSIGNED_INT", "Integer"),
    BIGINT("BIGINT", "Long"),
    UNSIGNED_LONG("UNSIGNED_LONG", "Long"),
    TINYINT("TINYINT", "Byte"),
    SMALLINT("SMALLINT", "Short"),
    UNSIGNED_SMALLINT("UNSIGNED_SMALLINT", "Short"),
    FLOAT("FLOAT", "Float"),
    UNSIGNED_FLOAT("UNSIGNED_FLOAT", "Float"),
    DOUBLE("DOUBLE", "Double"),
    UNSIGNED_DOUBLE("UNSIGNED_DOUBLE", "Double"),
    DECIMAL("DECIMAL", "BigDecimal"),
    BOOLEAN("BOOLEAN", "Boolean"),
    TIME("TIME", "Time"),
    DATE("DATE", "Date"),
    TIMESTAMP("TIMESTAMP", "Timestamp"),
    UNSIGNED_TIME("UNSIGNED_TIME", "Time"),
    UNSIGNED_DATE("UNSIGNED_DATE", "Date"),
    UNSIGNED_TIMESTAMP("UNSIGNED_TIMESTAMP", "Timestamp "),
    VARCHAR("VARCHAR", "String"),
    CHAR ("CHAR", "String"),
    ARRAY ("ARRAY", "Array"),
    ;

    /**
     * 数据库字段类型
     */
    private String columnType;
    /**
     * JAVA类型
     */
    private String javaType;

    public String getColumnType() {
        return columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    PhxDataTypeEnum(String columnType, String javaType) {
        this.columnType = columnType;
        this.javaType = javaType;
    }
}
