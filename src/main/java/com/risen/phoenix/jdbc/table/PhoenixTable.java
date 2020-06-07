package com.risen.phoenix.jdbc.table;

import java.util.List;

public class PhoenixTable {

    /**
     * 库名：默认RISEN
     */
    private String schem;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段映射信息
     */
    private List<PhoenixField> fields;

    public PhoenixTable(String tableName, List<PhoenixField> fields) {
        this.schem = "RISEN.";
        this.tableName = tableName;
        this.fields = fields;
    }

    public PhoenixTable(String schem, String tableName, List<PhoenixField> fields) {
        if (!schem.endsWith(".")){
            schem = schem + ".";
        }
        this.schem = schem;
        this.tableName = tableName;
        this.fields = fields;
    }

    public String getSchem() {
        return schem;
    }

    public void setSchem(String schem) {
        this.schem = schem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<PhoenixField> getFields() {
        return fields;
    }

    public void setFields(List<PhoenixField> fields) {
        this.fields = fields;
    }
}
