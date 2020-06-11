package com.risen.phoenix.jdbc.pojo;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;

import java.util.Date;

@PhxTabName(tableName = "PRODUCT", schem = "Ri")
public class Product {

    @PhxId(PhxDataTypeEnum.UNSIGNED_LONG)
    private Long uuid;

    @PhxField(PhxDataTypeEnum.BIGINT)
    private Long productUnLong;

    @PhxField(value = PhxDataTypeEnum.VARCHAR, length = 60)
    private String productName;

    @PhxField(value = PhxDataTypeEnum.CHAR, length = 80)
    private String productEmail;

    @PhxField(value = PhxDataTypeEnum.UNSIGNED_DATE)
    private Date gmtCreate;

    @PhxField(value = PhxDataTypeEnum.DATE)
    private Date gmtModify;

    @PhxField(value = PhxDataTypeEnum.FLOAT)
    private Float productFloat;

    @PhxField(value = PhxDataTypeEnum.DOUBLE)
    private Double productDouble;

    @PhxField(value = PhxDataTypeEnum.BOOLEAN)
    private Boolean productBoole;

    @PhxField(value = PhxDataTypeEnum.SMALLINT)
    private Short productShort;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getProductUnLong() {
        return productUnLong;
    }

    public void setProductUnLong(Long productUnLong) {
        this.productUnLong = productUnLong;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductEmail() {
        return productEmail;
    }

    public void setProductEmail(String productEmail) {
        this.productEmail = productEmail;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Float getProductFloat() {
        return productFloat;
    }

    public void setProductFloat(Float productFloat) {
        this.productFloat = productFloat;
    }

    public Double getProductDouble() {
        return productDouble;
    }

    public void setProductDouble(Double productDouble) {
        this.productDouble = productDouble;
    }

    public Boolean getProductBoole() {
        return productBoole;
    }

    public void setProductBoole(Boolean productBoole) {
        this.productBoole = productBoole;
    }

    public Short getProductShort() {
        return productShort;
    }

    public void setProductShort(Short productShort) {
        this.productShort = productShort;
    }

    @Override
    public String toString() {
        return "Product{" +
                "uuid=" + uuid +
                ", productUnLong=" + productUnLong +
                ", productName='" + productName + '\'' +
                ", productEmail='" + productEmail + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModify=" + gmtModify +
                ", productFloat=" + productFloat +
                ", productDouble=" + productDouble +
                ", productBoole=" + productBoole +
                ", productShort=" + productShort +
                '}';
    }
}
