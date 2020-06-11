package com.risen.phoenix.jdbc.util;


import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字符转换工具类
 */
public class CaseUtils {

    /**
     * 驼峰式命名，下划线风格并大写
     * createTime 转 CREATE_TIME
     * @param str createTime
     * @return CREATE_TIME
     */
    public static String lowerCamel(String str){
        if (str.equals(str.toUpperCase())){
            return str;
        }
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

    /**
     * jdbcTemplate转换为实体类
     * @param clazz 反射的java类
     * @param jdbcMapResult JDBC执行的返回结果
     * @return java类
     */
    public static <T> T reflect(Class<T> clazz, Map<String, Object> jdbcMapResult) {
        //获得
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldHasColumnAnnoMap = new LinkedHashMap<>(fields.length);
        for (Field field : fields) {
            fieldHasColumnAnnoMap.put(field.getName(), CaseUtils.lowerCamel(field.getName()));
        }
        Map<String, Object> conCurrent = new LinkedHashMap<>();
        for (Map.Entry<String, String> en : fieldHasColumnAnnoMap.entrySet()) {
            String key = en.getValue().toUpperCase();
            Object value = jdbcMapResult.get(key);
            if (value != null) {
                conCurrent.put(en.getKey(), jdbcMapResult.get(key));
            }
        }
        return JSON.parseObject(JSON.toJSONString(conCurrent), clazz);
    }
}
