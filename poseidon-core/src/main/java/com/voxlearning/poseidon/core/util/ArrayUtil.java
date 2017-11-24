package com.voxlearning.poseidon.core.util;

import java.lang.reflect.Array;
import java.util.Objects;


/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class ArrayUtil {

    /**
     * 向数组中添加内容
     *
     * @param buffer   已有数组
     * @param elements 要添加的元素
     * @param <T>      数据类型
     * @return
     */
    public static <T> T[] append(T[] buffer, T... elements) {
        if (isEmpty(elements)) {
            return buffer;
        }
        T[] newArray = resize(buffer, buffer.length + elements.length);
        System.arraycopy(buffer, 0, newArray, buffer.length, elements.length);
        return newArray;
    }

    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(final T... array) {
        return !isEmpty(array);
    }

    public static <T> T[] resize(T[] buffer, int newSize) {
        return resize(buffer, newSize, buffer.getClass().getComponentType());
    }

    public static <T> T[] resize(T[] buffer, int newSize, Class<?> componetType) {
        T[] newArray = newArray(componetType, newSize);
        if (Objects.nonNull(buffer)) {
            System.arraycopy(buffer, 0, newArray, 0, Math.min(buffer.length, newSize));
        }
        return newArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> compentType, int newSize) {
        return (T[]) Array.newInstance(compentType, newSize);
    }


    public static <T> String arrayToString(T[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (T t : array) {
            builder.append(t.toString()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }


    public static <T> T[] clone(T[] array) {
        if (Objects.isNull(array)) {
            return null;
        }
        return array.clone();

    }

    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (isArray(obj)) {
            Object result;
            Class<?> componentType = obj.getClass().getComponentType();
            //基本类型
            if (componentType.isPrimitive()) {
                int length = Array.getLength(obj);
                result = Array.newInstance(componentType, length);
                while (length-- > 0) {
                    Array.set(result, length, Array.get(obj, length));
                }
            } else {
                result = ((Object[]) obj).clone();
            }
            return (T) result;
        }
        return null;
    }


    public static <T> boolean isArray(T obj) {
        return obj.getClass().isArray();
    }

}
