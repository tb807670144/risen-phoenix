package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;
import com.risen.phoenix.jdbc.pojo.Apple;
import com.risen.phoenix.jdbc.pojo.Student;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TestClass {

    /**
     * 反射获取【字段名称】和【字段值】
     */
    public static void method1() throws Exception{
        Student student = new Student();
        student.setId(1);
        student.setName("小米雷军");
        student.setUserType("99999");

        Field[] fields = student.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName() + "*************" + field.get(student));
        }
    }

    /**
     * 反射获取类上的【注解信息】
     */
    public static void method2() throws Exception{
        Class<Student> clz = Student.class;

        //查看类上是否有注解
        boolean classAn = clz.isAnnotationPresent(PhxTabName.class);
        if (classAn){
            PhxTabName annotation = clz.getAnnotation(PhxTabName.class);
            System.out.println(annotation.tableName());
            System.out.println(annotation.schem());
            System.out.println(annotation.upLower());
        }

        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            System.out.println(name);
            boolean fieldAn = field.isAnnotationPresent(PhxField.class);
            if (fieldAn){
                PhxField annotation = field.getAnnotation(PhxField.class);
                PhxDataTypeEnum value = annotation.value();
                System.out.println(value.getColumnType());
            }
        }

    }

    /**
     * 反射获取【字段类型】
     * @throws Exception
     */
    public static void method3() throws Exception{
        Class<Apple> clazz = Apple.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getType().getName());
        }

    }

    public static void main(String[] args) throws Exception{
        TestClass.method3();

    }
}
