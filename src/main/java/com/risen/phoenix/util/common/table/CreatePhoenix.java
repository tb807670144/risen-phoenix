package com.risen.phoenix.util.common.table;

import java.util.List;

public class CreatePhoenix {

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
     * 是否含有主键
     * 交给程序控制
     */
    private boolean priKey;

    public CreatePhoenix(String tableName, List<PhoenixField> fields) {
        this.schem = "RISEN.";
        this.tableName = tableName;
        this.fields = fields;
    }

    public CreatePhoenix(String schem, String tableName, List<PhoenixField> fields) {
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

    /**
     * 构建建表语句
     */
    public String buildCreateSQL(){
        StringBuffer sql = new StringBuffer("CREATE TABLE " + this.schem.toUpperCase() + this.tableName.toUpperCase() + "(");
        StringBuffer primarySql = new StringBuffer();
        int i = 0, size = fields.size();
        for (PhoenixField field : fields) {
            i++;
            sql.append(field.getFieldName()).append(" ").append(field.getFieldType()).append(" ").append(field.getCanNull() ? "NOT NULL" : "");
            if (i != size){
                sql.append(",");
            }
            if (field.getPrimaryKey()){
                this.priKey = true;
                primarySql.append(field.getFieldName()).append(",");
            }
        }
        if (priKey){
            String str = primarySql.substring(0, primarySql.length() - 1);
            sql.append(buildPrimaryKeySql(str));
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 构建主键语句
     * @param str 字段名称集合
     */
    private StringBuffer buildPrimaryKeySql(String str){
        StringBuffer primaryKeySql = new StringBuffer();
        return primaryKeySql.append("  CONSTRAINT PK PRIMARY KEY (").append(str).append("  )");
    }
}
