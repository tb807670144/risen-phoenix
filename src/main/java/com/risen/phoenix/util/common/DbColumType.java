package com.risen.phoenix.util.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public enum DbColumType {
    /**
     * INTEGER
     */
    INTEGER("INTEGER"),
    VARCHAR("VARCHAR(20)"),
    BIGINT("BIGINT"),


    ;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 获取类型
     */
//    public abstract String getType();

    DbColumType(String columnType) {
        this.columnType = columnType;
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
        String ss = DbColumType.getJavaType("varchar");
        System.out.println(ss);
    }
}
