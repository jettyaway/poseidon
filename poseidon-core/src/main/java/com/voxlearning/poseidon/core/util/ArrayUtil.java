package com.voxlearning.poseidon.core.util;

import com.voxlearning.poseidon.core.exceptions.UtilException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;


/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class ArrayUtil {

    public static final int INDEX_NOT_FOUND = -1;

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

    /**
     * 包装数组对象
     *
     * @param obj 对象，可以是对象数组或者基本类型数组
     * @return 包装类型数组或对象数组
     * @throws UtilException 对象为非数组
     */
    public static Object[] wrap(Object obj) {
        if (isArray(obj)) {
            try {
                return (Object[]) obj;
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return wrap((long[]) obj);
                    case "int":
                        return wrap((int[]) obj);
                    case "short":
                        return wrap((short[]) obj);
                    case "char":
                        return wrap((char[]) obj);
                    case "byte":
                        return wrap((byte[]) obj);
                    case "boolean":
                        return wrap((boolean[]) obj);
                    case "float":
                        return wrap((float[]) obj);
                    case "double":
                        return wrap((double[]) obj);
                    default:
                        throw new UtilException(e);
                }
            }
        }
        throw new UtilException(StrUtil.format("[{}] is not Array!", obj.getClass()));
    }


    //--------------------------------------------------------------join

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param <T>         被处理的集合
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(T[] array, CharSequence conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            if (ArrayUtil.isArray(item)) {
                sb.append(join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(CollectionUtil.join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(CollectionUtil.join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(long[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (long item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(int[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(short[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (short item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(char[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (char item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(byte[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (byte item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(boolean[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (boolean item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(float[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (float item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(double[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (double item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(Object array, CharSequence conjunction) {
        if (isArray(array)) {
            final Class<?> componentType = array.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                final String componentTypeName = componentType.getName();
                switch (componentTypeName) {
                    case "long":
                        return join((long[]) array, conjunction);
                    case "int":
                        return join((int[]) array, conjunction);
                    case "short":
                        return join((short[]) array, conjunction);
                    case "char":
                        return join((char[]) array, conjunction);
                    case "byte":
                        return join((byte[]) array, conjunction);
                    case "boolean":
                        return join((boolean[]) array, conjunction);
                    case "float":
                        return join((float[]) array, conjunction);
                    case "double":
                        return join((double[]) array, conjunction);
                    default:
                        throw new UtilException("Unknown primitive type: [{}]", componentTypeName);
                }
            } else {
                return join((Object[]) array, conjunction);
            }
        }
        throw new UtilException(StrUtil.format("[{}] is not a Array!", array.getClass()));
    }

    // -----------------------------------------------------------------------------contains

    /**
     * 数组中是否包含元素
     *
     * @param array 数组
     * @param c     被检查的元素
     * @return 是否包含
     */
    public static boolean contains(char[] array, char c) {
        return indexOf(array, c) > INDEX_NOT_FOUND;
    }


    /**
     * 返回数组中指定元素所在的第一个位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param array 数组
     * @param c     被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     */
    public static int indexOf(char[] array, char c) {
        if (Objects.isNull(array)) {
            return INDEX_NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == c) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param array 数组
     * @param c     被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     */
    public static int lastIndexOf(char[] array, char c) {
        if (Objects.isNull(array)) {
            return INDEX_NOT_FOUND;
        }
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == c) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 数组中是否包含元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) > INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     */
    public static <T> int indexOf(T[] array, Object value) {
        for (int i = 0; i < array.length; i++) {
            if (ObjectUtil.equal(value, array[i])) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在位置，忽略大小写，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     * @since 3.1.2
     */
    public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
        for (int i = 0; i < array.length; i++) {
            if (StrUtil.equalsIgnoreCase(array[i], value)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     * @since 3.0.7
     */
    public static <T> int lastIndexOf(T[] array, Object value) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (ObjectUtil.equal(value, array[i])) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

}
