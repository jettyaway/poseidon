package com.voxlearning.poseidon.core.convert;


import com.voxlearning.poseidon.core.convert.converter.EnumConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-28
 * @since 17-11-28
 */
public class Convert {

    private ConvertFactory convertFactory = ConvertFactory.getInstance();


    public static String toStr(Object value, String defaultValue) {
        return convert(String.class, value, defaultValue);
    }

    public static String toStr(Object value) {
        return convert(String.class, value);
    }

    public static String[] toStrArray(Object value) {
        return convert(String[].class, value);
    }

    public static Character toChar(Object value, Character defaultValue) {
        return convert(Character.class, value, defaultValue);
    }

    public static Character toChar(Object value) {
        return convert(Character.class, value, null);
    }

    public static Character[] toCharArray(Object value) {
        return convert(Character[].class, value);
    }

    public static Byte toByte(Object value, Byte defaultValue) {
        return convert(Byte.class, value, defaultValue);
    }

    public static Byte toByte(Object value) {
        return convert(Byte.class, value, null);
    }

    public static Byte[] toByteArray(Object value) {
        return convert(Byte[].class, value, null);
    }

    public static Short toShort(Object value, Short defaultValue) {
        return convert(Short.class, value, defaultValue);
    }

    public static Short toShort(Object value) {
        return convert(Short.class, value, null);
    }

    public static Short[] toShortArray(Object value) {
        return convert(Short[].class, value);
    }

    public static Number toNumber(Object value, Number defauleValue) {
        return convert(Number.class, value, defauleValue);
    }

    public static Number toNumber(Object value) {
        return convert(Number.class, value);
    }

    public static Number[] toNumberArray(Object value) {
        return convert(Number[].class, value);
    }

    public static Integer toInt(Object value, Integer defaultValue) {
        return convert(Integer.class, value, defaultValue);
    }

    public static Integer toInt(Object value) {
        return convert(Integer.class, value);
    }

    public static Integer[] toIntArray(Object value) {
        return convert(Integer[].class, value);
    }

    public static Long toLong(Object value, Long defaultValue) {
        return convert(Long.class, value, defaultValue);
    }

    public static Long toLong(Object value) {
        return convert(Long.class, value);
    }

    public static Long toLongArray(Object value) {
        return convert(Long.class, value);
    }

    public static Float toFloat(Object value, Float defaultValue) {
        return convert(Float.class, value, defaultValue);
    }

    public static Float toFloat(Object value) {
        return convert(Float.class, value);
    }

    public static Float[] toFloatArray(Object value) {
        return convert(Float[].class, value);
    }

    public static Double toDouble(Object value, Double defaultValue) {
        return convert(Double.class, value, defaultValue);
    }

    public static Double toDouble(Object value) {
        return convert(Double.class, value);
    }

    public static Double[] toDoubleArray(Object value) {
        return convert(Double[].class, value);
    }

    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        return convert(Boolean.class, value, defaultValue);
    }

    public static Boolean toBoolean(Object value) {
        return convert(Boolean.class, value);
    }

    public static Boolean[] toBooleanArray(Object value) {
        return convert(Boolean[].class, value);
    }

    public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
        return convert(BigInteger.class, value, defaultValue);
    }

    public static BigInteger toBigInteger(Object value) {
        return convert(BigInteger.class, value);
    }

    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        return convert(BigDecimal.class, value, defaultValue);
    }

    public static BigDecimal toBigDecimal(Object value) {
        return convert(BigDecimal.class, value);
    }

    //TODO ADD ENUM and COLLECTION converter

    /**
     * 将对象转化为枚举对象<br/>
     * 如果给定的值为空或者转化失败则返回默认值<br/>
     *
     * @param clazz        枚举的Class
     * @param value        值
     * @param defaultValue 默认值
     * @param <E>          枚举的类型
     * @return 转换后的枚举类型 {@link Enum}
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
        return (new EnumConverter<>(clazz)).convert(value, defaultValue);
    }

    /**
     * 将对象转化为枚举对象<br/>
     * 如果给定的值为空或者转化失败则返回默认值<code>null</code><br/>
     *
     * @param clazz 枚举的Class
     * @param value 值
     * @param <E>   枚举的类型
     * @return 转换后的枚举类型 {@link Enum}
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
        return toEnum(clazz, value, null);
    }

    // ------------------------------------------------------hex 十六进制------------------------------------------------


    public static <T> T convert(Class<T> type, Object value) {
        return convert(type, value, null);
    }


    public static <T> T convert(Class<T> type, Object value, T defaultValue) {
        return ConvertFactory.getInstance().convert(type, value, defaultValue);
    }
}
