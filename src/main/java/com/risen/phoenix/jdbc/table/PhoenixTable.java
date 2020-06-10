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
    /**
     * 加盐数
     */
    private int salt;
    /**
     * 是否压缩
     */
    private boolean compression;

    public PhoenixTable(String schem, String tableName, List<PhoenixField> fields, int salt, boolean compression) {
        this.schem = schem;
        this.tableName = tableName;
        this.fields = fields;
        this.salt = salt;
        this.compression = compression;
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

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }
}
