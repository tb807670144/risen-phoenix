package com.risen.phoenix.jdbc.pojo;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;

import java.util.Date;

@PhxTabName(tableName = "apple", schem = "cct")
public class Apple{

    @PhxId(PhxDataTypeEnum.INTEGER)
    private Integer uuid;

    @PhxField(PhxDataTypeEnum.VARCHAR)
    private String name;

    @PhxField(PhxDataTypeEnum.DATE)
    private Date gmtCreate;

    @PhxField(PhxDataTypeEnum.FLOAT)
    private float aFloat;

    @PhxField(PhxDataTypeEnum.DOUBLE)
    private double aDouble;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", aFloat=" + aFloat +
                ", aDouble=" + aDouble +
                '}';
    }
}
