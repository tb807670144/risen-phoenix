package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;
import com.risen.phoenix.jdbc.pojo.Apple;
import com.risen.phoenix.jdbc.pojo.Student;
import org.apache.phoenix.parse.HintNode;
import org.apache.phoenix.util.ColumnInfo;
import org.apache.phoenix.util.QueryUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Phoenix工具类
     * @see QueryUtil
     * @throws Exception
     */
    public static void method4() throws Exception{
        List<ColumnInfo> list = new ArrayList<>();
        ColumnInfo columnInfo = new ColumnInfo("USER_UUID", 3);
        ColumnInfo columnInfo2 = new ColumnInfo("USER_NAME", 3);
        list.add(columnInfo);
        list.add(columnInfo2);

        List<String> strList = new ArrayList<>();
        strList.add("字符串1");
        strList.add("字符串1");

        String tableName = QueryUtil.constructSelectStatement("tableName", list, "2131321");
        System.out.println(tableName);
        String tabName = QueryUtil.constructGenericUpsertStatement("tabName", 3);
        System.out.println(tabName);
        String table_name = QueryUtil.constructUpsertStatement("TABLE_NAME", list);
        System.out.println(table_name);
        String s = QueryUtil.constructParameterizedInClause(1, 6);
        System.out.println(s);
        String ttt = QueryUtil.constructSelectStatement("tableName", strList, "USER_NAME = 1 AND USER_TYPE = 5", HintNode.Hint.SEEK_TO_COLUMN, false);
        System.out.println(ttt);
    }


    public static void method5() throws Exception{

        Integer offsetLimit = QueryUtil.getOffsetLimit(1, 5);
        String viewStatement = QueryUtil.getViewStatement("RISEN", "table_name", "USER_NAME = 213");
        System.out.println(viewStatement);
    }

    public static void main(String[] args) throws Exception{
        TestClass.method5();
    }
}
