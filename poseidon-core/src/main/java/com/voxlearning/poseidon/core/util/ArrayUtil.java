package com.voxlearning.poseidon.core.util;

import com.voxlearning.poseidon.core.exceptions.UtilException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;


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

    /**
     * 强制转化数组
     * 转化后生成一个新的数组
     *
     * @param type 　数组类型或者数组元素类型
     * @param obj  　原数组
     * @return 转化后的数组
     */
    public static Object[] cast(Class<?> type, Object obj) {
        if (Objects.isNull(obj)) {
            throw new NullPointerException("origin array can not be null");
        }
        if (!obj.getClass().isArray()) {
            throw new IllegalArgumentException("Argument obj must be array.");
        }
        if (Objects.isNull(type)) {
            return (Object[]) obj;
        }
        Class<?> typeClass = type.isArray() ? type.getComponentType() : type;
        Object[] objArray = (Object[]) obj;
        Object[] result = ArrayUtil.newArray(typeClass, objArray.length);
        System.arraycopy(objArray, 0, result, 0, objArray.length);
        return result;
    }

    /**
     * 将数组或者集合转化为{@link String}
     *
     * @param object 集合或者数组对象
     * @return {@link String}
     */
    public static String toString(Object object) {
        if (ArrayUtil.isArray(object)) {
            try {
                return Arrays.deepToString((Object[]) object);
            } catch (RuntimeException e) {
                String className = object.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return Arrays.toString((long[]) object);
                    case "int":
                        return Arrays.toString((int[]) object);
                    case "short":
                        return Arrays.toString((short[]) object);
                    case "byte":
                        return Arrays.toString((byte[]) object);
                    case "float":
                        return Arrays.toString((float[]) object);
                    case "double":
                        return Arrays.toString((double[]) object);
                    case "char":
                        return Arrays.toString((char[]) object);
                    case "boolean":
                        return Arrays.toString((boolean[]) object);
                    default:
                        throw new UtilException(e, "error type of array");
                }
            }
        }
        return object.toString();
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

    /**
     * 克隆数组对象 如果不是对象则返回<code>null</code>
     *
     * @param obj 对象
     * @param <T> 类型
     * @return 克隆后的数组对象
     */
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


    /**
     * 判断是否为数组
     *
     * @param obj 对象
     * @param <T> 泛型类型
     * @return 是否为数组 true:是 false:不是
     */
    public static <T> boolean isArray(T obj) {
        return obj.getClass().isArray();
    }

    //------------------------------------wrap and unwrap --------------------------------------------------------------


    /**
     * int 类型数组 转到Integer数组
     *
     * @param values int 数组
     * @return Integer[] {@link Integer}
     */
    public static Integer[] wrap(int... values) {
        if (Objects.isNull(values)) {
            return null;
        }
        int len = values.length;
        Integer[] wraps = new Integer[len];
        for (int i = 0; i < len; i++) {
            wraps[i] = Integer.valueOf(values[i]);
        }
        return wraps;
    }

    /**
     * Integer 类型数组 转到int数组
     *
     * @param values
     * @return int[]
     */
    public static int[] unWrap(Integer... values) {
        if (Objects.isNull(values)) {
            return null;
        }
        int len = values.length;
        int[] unWrap = new int[len];
        for (int i = 0; i < len; i++) {
            unWrap[i] = values[i].intValue();
        }
        return unWrap;
    }


}
