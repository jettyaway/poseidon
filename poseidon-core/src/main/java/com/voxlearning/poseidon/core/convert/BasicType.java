package com.voxlearning.poseidon.core.convert;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/25
 * @since 2017/11/25
 */
public enum BasicType {
    BYTE, SHORT, INT, INTEGER, LONG, DOUBLE, FLOAT, BOOLEAN, CHAR, CHARACTER, STRING;

    public static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new ConcurrentHashMap<>(8);
    public static final Map<Class<?>, Class<?>> primitiveWrapperMap = new ConcurrentHashMap<>(8);

    static {
        wrapperPrimitiveMap.put(Byte.class, byte.class);
        wrapperPrimitiveMap.put(Character.class, char.class);
        wrapperPrimitiveMap.put(Integer.class, int.class);
        wrapperPrimitiveMap.put(Short.class, short.class);
        wrapperPrimitiveMap.put(Float.class, float.class);
        wrapperPrimitiveMap.put(Long.class, long.class);
        wrapperPrimitiveMap.put(Double.class, double.class);
        wrapperPrimitiveMap.put(Boolean.class, boolean.class);
        primitiveWrapperMap.putAll(wrapperPrimitiveMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
    }

    /**
     * 基本类型到包装类型 不存在则返回基本类型
     *
     * @param clazz 基本类型
     * @return 包装类
     */
    public static Class<?> wrap(Class<?> clazz) {
        if (Objects.isNull(clazz) || !clazz.isPrimitive()) {
            return clazz;
        }
        Class<?> wrapperClazz = primitiveWrapperMap.get(clazz);
        return Objects.isNull(wrapperClazz) ? clazz : wrapperClazz;
    }

    /**
     * 包装类型到原始类型 不存在则返回基本类型
     *
     * @param clazz 包装类型
     * @return 基本类型
     */
    public static Class<?> unWrap(Class<?> clazz) {
        if (Objects.isNull(clazz) || clazz.isPrimitive()) {
            return clazz;
        }
        Class<?> primitiveClazz = wrapperPrimitiveMap.get(clazz);
        return Objects.isNull(primitiveClazz) ? clazz : primitiveClazz;
    }

}
