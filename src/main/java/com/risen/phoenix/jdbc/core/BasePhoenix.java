package com.risen.phoenix.jdbc.core;

import com.google.common.base.CaseFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PhoenixJDBC的抽象类
 */
public abstract class BasePhoenix {

    /**
     * 驼峰式命名，下划线风格并大写
     * createTime 转 CREATE_TIME
     * @param str createTime
     * @return CREATE_TIME
     */
    protected static String lowerCamel(String str){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str).toUpperCase();
    }

    /**
     * 驼峰式命名，转java风格
     * CREATE_TIME 转 createTime
     * @param str CREATE_TIME
     * @return createTime
     */
    protected static String camelLower(String str){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    /**
     * 传输的值处理
     * @param type java类型
     * @param data 数据值
     * @param flat 返回时追加的字符串
     */
    protected Object commaNorm(String type, Object data, String flat){
        if (data == null){
            return data;
        }
        switch (type){
            case "String":
                return String.format("'%s'%s", data, flat);
            case "Date":
                Date date = (Date) data;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = dateFormat.format(date);
                return String.format("TO_DATE('%s')%s", format, flat);
            default:
                return data + flat;

        }
    }

}
