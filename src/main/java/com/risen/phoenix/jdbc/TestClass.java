package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;
import com.risen.phoenix.jdbc.pojo.Student;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TestClass {

    public static void main(String[] args) throws Exception{

        Student student = new Student();
        student.setId(1);
        student.setName("小米雷军");
        student.setUserType("99999");
        Field[] fields = student.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName() + "*************" + field.get(student));
        }


//        Class<Student> clz = Student.class;
//
//        //查看类上是否有注解
//        boolean classAn = clz.isAnnotationPresent(PhxTabName.class);
//        if (classAn){
//            PhxTabName annotation = clz.getAnnotation(PhxTabName.class);
//            System.out.println(annotation.tableName());
//            System.out.println(annotation.schem());
//            System.out.println(annotation.upLower());
//        }
//
//        Field[] fields = clz.getDeclaredFields();
//        for (Field field : fields) {
//            String name = field.getName();
//            System.out.println(name);
//            boolean fieldAn = field.isAnnotationPresent(PhxField.class);
//            if (fieldAn){
//                PhxField annotation = field.getAnnotation(PhxField.class);
//                PhxDataTypeEnum[] value = annotation.value();
//                for (PhxDataTypeEnum phxDataTypeEnum : value) {
//                    System.out.println(phxDataTypeEnum.getColumnType());
//                }
//            }
//        }

        /*Annotation[] annotations = studentClass.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(PhxTabName.class)){
                    method.invoke(new Student());
                    PhxTabName annotation1 = method.getAnnotation(PhxTabName.class);
                    String s = annotation1.tableName();
                    System.out.println(s);
                }
                *//*Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    System.out.println(parameter.getName());
                }*//*
//                Object defaultValue = method.getDefaultValue();
//                System.out.println(defaultValue);
//                System.out.println(method.getName());
                System.out.println("---------------");
            }
        }*/
    }
}
