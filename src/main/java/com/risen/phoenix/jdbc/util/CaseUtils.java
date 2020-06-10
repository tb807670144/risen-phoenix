package com.risen.phoenix.jdbc.util;


import com.google.common.base.CaseFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Phoenix工具类
 */
public class CaseUtils {

    /**
     * 驼峰式命名，下划线风格并大写
     * createTime 转 CREATE_TIME
     * @param str createTime
     * @return CREATE_TIME
     */
    public static String lowerCamel(String str){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str).toUpperCase();
    }

    /**
     * 驼峰式命名，转java风格
     * CREATE_TIME 转 createTime
     * @param str CREATE_TIME
     * @return createTime
     */
    public static String camelLower(String str){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    public static String commaNorm(String type, Object data, String flat) {
        if (data == null) {
            return null;
        }
        switch (type) {
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
