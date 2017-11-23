package com.voxlearning.poseidon.core.util;


import com.voxlearning.poseidon.core.exceptions.UtilException;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.lang.SimpleCache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class ReflectUtil {

    private static final SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTOR_CACHE = new SimpleCache<>();

    private static final SimpleCache<Class<?>, Method[]> METHOD_CACHE = new SimpleCache<>();

    private static final SimpleCache<Class<?>, Field[]> FIELD_CACHE = new SimpleCache<>();

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        try {
            return clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取类的所有构造器【default private protected public 】
     *
     * @param clazz 类型
     * @param <T>   泛型类
     * @return 构造器
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructor(Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        Constructor<?>[] constructors = CONSTRUCTOR_CACHE.get(clazz);
        if (Objects.nonNull(constructors)) {
            return (Constructor<T>[]) constructors;
        }
        constructors = getConstructorsDirectly(clazz);
        return (Constructor<T>[]) CONSTRUCTOR_CACHE.put(clazz, constructors);
    }

    /**
     * 获取构造函数
     *
     * @param clazz 类型
     * @return 字段数组
     */
    public static Constructor<?>[] getConstructorsDirectly(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        return clazz.getDeclaredConstructors();
    }


    //-------------------------------FIELD------------------------------------------------------------------------------

    public static Field getField(Class<?> clazz, String name) {
        Preconditions.checkArgument(Objects.nonNull(clazz) && Objects.nonNull(name));
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || fieldName == null || "".equals(fieldName)) {
            return null;
        }
        return getFieldValue(obj, getField(obj.getClass(), fieldName));
    }

    public static Object getFieldValue(Object obj, Field field) {
        if (Objects.isNull(obj) || Objects.isNull(field)) {
            return null;
        }
        field.setAccessible(true);
        Object res;
        try {
            res = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new UtilException(e);
        }
        return res;
    }

    public static void setFieldValue(Object obj, String fileName, Object value) {
        Preconditions.checkNotNull(obj);
        Preconditions.notBlank(fileName);
        setFieldValue(obj, getField(obj.getClass(), fileName), value);
    }

    public static void setFieldValue(Object obj, Field field, Object value) {
        Preconditions.checkNotNull(obj);
        Preconditions.checkNotNull(field);
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 获取指定类所有的字段 包含父类中的
     * 该方法先从缓存中获取
     *
     * @param clazz 类型
     * @return
     */
    public static Field[] getFields(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        Field[] fields = FIELD_CACHE.get(clazz);
        if (Objects.nonNull(fields)) {
            return fields;
        }
        fields = getFieldsDirectly(clazz, true);
        return FIELD_CACHE.put(clazz, fields);
    }

    /**
     * 获取指定类所有的字段
     *
     * @param clazz      类型
     * @param superClass 是否包含父类中的字段
     * @return
     */
    public static Field[] getFieldsDirectly(Class<?> clazz, boolean superClass) {
        Field[] declaredFields;
        Field[] allField = null;
        Class<?> superClazz = clazz;
        while (Objects.nonNull(superClazz)) {
            declaredFields = superClazz.getDeclaredFields();
            if (Objects.isNull(allField)) {
                allField = declaredFields;
            } else {
                allField = ArrayUtil.append(allField, declaredFields);
            }
            superClazz = superClass ? clazz.getSuperclass() : null;
        }
        return allField;
    }

    //------------------------------method------------------------------------------------------------------------------


    public static Method getMethods(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) {
        Preconditions.checkNotNull(clazz);
        Preconditions.notBlank(methodName);
        Method[] methods = getMethods(clazz);
        if (ArrayUtil.isEmpty(methods)) {
            return null;
        }
        List<Method> collect = Stream.of(methods).filter(
                method -> StrUtil.equals(method.getName(), methodName, ignoreCase) &&
                        ClassUtil.isAllAssignablFrom(method.getParameterTypes(), paramTypes))
                .collect(Collectors.toList());
        return collect.get(0);


    }

    /**
     * 获取符合条件的方法
     *
     * @param clazz     类型
     * @param predicate 过滤器
     * @return Method 数组
     * @see Predicate
     */
    public static Method[] getMethods(Class<?> clazz, Predicate<Method> predicate) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        Method[] methods = getMethods(clazz);
        if (Objects.isNull(predicate)) {
            return methods;
        }
        List<Method> methodList = Stream.of(methods).filter(predicate).collect(Collectors.toList());
        return methodList.toArray(new Method[methodList.size()]);
    }

    /**
     * 获取改类所有的方法包括父类
     * 该方法先从缓存中获取
     *
     * @param clazz 类型
     * @return Method 数组
     */
    public static Method[] getMethods(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        Method[] methods = METHOD_CACHE.get(clazz);
        if (Objects.nonNull(methods)) {
            return methods;
        }
        Method[] allMethods = getMethodDriectly(clazz, true);
        return METHOD_CACHE.put(clazz, allMethods);
    }

    /**
     * 返回指定类所有的方法
     *
     * @param clazz                类型
     * @param withSuperClassMethod 是否包含父类中的方法
     * @return
     */
    public static Method[] getMethodDriectly(Class<?> clazz, boolean withSuperClassMethod) {
        Preconditions.checkNotNull(clazz);
        Class<?> searchType = clazz;
        Method[] allMethods = null;
        Method[] declaredMethods;
        while (Objects.nonNull(searchType)) {
            declaredMethods = clazz.getDeclaredMethods();
            if (Objects.isNull(allMethods)) {
                allMethods = declaredMethods;
            } else {
                allMethods = ArrayUtil.append(allMethods, declaredMethods);
            }
            searchType = withSuperClassMethod ? searchType.getSuperclass() : null;
        }
        return allMethods;
    }


    public static void main(String[] args) {
        System.out.println(ReflectUtil.getConstructor(SimpleCache.class));
    }


}
