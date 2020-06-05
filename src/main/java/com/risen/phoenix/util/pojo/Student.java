package com.risen.phoenix.util.pojo;

public class Student {

    private Integer id;
    private String name;
    private String userType;

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
