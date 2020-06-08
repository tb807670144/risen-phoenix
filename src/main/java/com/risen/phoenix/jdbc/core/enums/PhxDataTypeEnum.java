package com.risen.phoenix.jdbc.core.enums;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

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

    public static String getJavaType(String columnType){
        columnType = columnType.toUpperCase();
        switch (columnType){
            case "INTEGER":
            case "UNSIGNED_INT":
                return "java.lang.Integer";
            case "BIGINT":
            case "UNSIGNED_LONG":
                return "java.lang.Long";
            case "TINYINT":
            case "UNSIGNED_TINYINT":
                return "java.lang.Byte";
            case "SMALLINT":
            case "UNSIGNED_SMALLINT":
                return "java.lang.Short";
            case "FLOAT":
            case "UNSIGNED_FLOAT":
                return "java.lang.Float";
            case "DOUBLE":
            case "UNSIGNED_DOUBLE":
                return "java.lang.Double";
            case "DECIMAL":
                return "java.lang.Long";
            case "BOOLEAN":
                return "java.lang.Boolean";
            case "TIME":
                return "";
            case "DATE":
                return "java.lang.Boolean";
            case "TIMESTAMP":
                return "java.lang.Boolean";
            case "UNSIGNED_TIME":
                return "java.lang.Boolean";
            case "UNSIGNED_DATE":
                return "java.lang.Boolean";
            case "UNSIGNED_TIMESTAMP":
                return "java.lang.Boolean";
            case "VARCHAR":
                return "java.lang.Boolean";
            case "CHAR":
            case "BINARY":
            case "VARBINARY":
                return "java.lang.Boolean";
            default:
        }
        throw new RuntimeException("未知匹配类型");
    }

    public static String getPhoenixType(String columnType){
        switch (columnType){
            case "java.lang.Integer":
                return "INTEGER";
            case "java.lang.String":
                return "VARCHAR";
            case "java.lang.Boolean":
                return "BOOLEAN";
            case "java.lang.Long":
                return "BIGINT";
            case "java.lang.Float":
                return "FLOAT";
            case "java.lang.Double":
                return "DOUBLE";
            default:
        }
        throw new RuntimeException("未知匹配类型");
    }

    public static void main(String[] args) throws Exception{
        Statement statement = null;
        Object model = null;
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i < columnCount; i++){
                String columnName = metaData.getColumnName(i);
            }
        }
        String ss = PhxDataTypeEnum.getJavaType("varchar");
        System.out.println(ss);
    }
}
