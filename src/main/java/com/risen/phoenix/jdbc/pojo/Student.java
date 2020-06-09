package com.risen.phoenix.jdbc.pojo;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;

public class Student{

    @PhxId(PhxDataTypeEnum.VARCHAR)
    private Integer id;
    @PhxField(value = PhxDataTypeEnum.VARCHAR, length = 20)
    private String name;
    @PhxField(PhxDataTypeEnum.INTEGER)
    private String userType;
    private String userType2;
    private String userType3;
    private String userType4;

    public Student() {
    }

    public Student(Integer id, String name, String userType) {
        this.id = id;
        this.name = name;
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
