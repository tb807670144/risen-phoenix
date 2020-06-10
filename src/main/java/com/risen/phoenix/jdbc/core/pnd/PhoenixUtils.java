package com.risen.phoenix.jdbc.core.pnd;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class PhoenixUtils {

    public static final Pattern PROPERTY = Pattern.compile("([a-z])([A-Z])");
    public static final Pattern COLUMN = Pattern.compile("_([a-zA-Z])");
    public static final Pattern CHECK = Pattern.compile("[0-9A-Za-z]{2,64}?");
    public static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([\\w\\.\\[\\]]+)\\}");
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap(8);
    private static final Map<Character, Character> NINE_KEY_CODE_MAPPER = new HashMap(32);

    static {
        primitiveWrapperTypeMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperTypeMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperTypeMap.put(Character.TYPE, Character.class);
        primitiveWrapperTypeMap.put(Double.TYPE, Double.class);
        primitiveWrapperTypeMap.put(Float.TYPE, Float.class);
        primitiveWrapperTypeMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperTypeMap.put(Long.TYPE, Long.class);
        primitiveWrapperTypeMap.put(Short.TYPE, Short.class);
        NINE_KEY_CODE_MAPPER.put('A', '2');
        NINE_KEY_CODE_MAPPER.put('B', '2');
        NINE_KEY_CODE_MAPPER.put('C', '2');
        NINE_KEY_CODE_MAPPER.put('D', '3');
        NINE_KEY_CODE_MAPPER.put('E', '3');
        NINE_KEY_CODE_MAPPER.put('F', '3');
        NINE_KEY_CODE_MAPPER.put('G', '4');
        NINE_KEY_CODE_MAPPER.put('H', '4');
        NINE_KEY_CODE_MAPPER.put('I', '4');
        NINE_KEY_CODE_MAPPER.put('J', '5');
        NINE_KEY_CODE_MAPPER.put('K', '5');
        NINE_KEY_CODE_MAPPER.put('L', '5');
        NINE_KEY_CODE_MAPPER.put('M', '6');
        NINE_KEY_CODE_MAPPER.put('N', '6');
        NINE_KEY_CODE_MAPPER.put('O', '6');
        NINE_KEY_CODE_MAPPER.put('P', '7');
        NINE_KEY_CODE_MAPPER.put('Q', '7');
        NINE_KEY_CODE_MAPPER.put('R', '7');
        NINE_KEY_CODE_MAPPER.put('S', '7');
        NINE_KEY_CODE_MAPPER.put('T', '8');
        NINE_KEY_CODE_MAPPER.put('U', '8');
        NINE_KEY_CODE_MAPPER.put('V', '8');
        NINE_KEY_CODE_MAPPER.put('W', '9');
        NINE_KEY_CODE_MAPPER.put('X', '9');
        NINE_KEY_CODE_MAPPER.put('Y', '9');
        NINE_KEY_CODE_MAPPER.put('Z', '9');
    }

    public PhoenixUtils() {
    }

    public static String mapperColName(String propName) {
        return PROPERTY.matcher(propName).replaceAll("$1_$2").toUpperCase();
    }

    public static <T> T getValue(String value, T dv) {
        if (StringUtils.hasText(value)) {
            Class<?> toClass = dv == null ? Object.class : dv.getClass();
            if (toClass == String.class){
                String result;
                if (value == null) {
                    result = "null";
                } else {
                    result = value.toString();
                    return (T)result;
                }
            }
        }
        return dv;
    }

}
