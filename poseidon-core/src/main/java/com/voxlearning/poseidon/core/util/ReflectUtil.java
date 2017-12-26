package com.voxlearning.poseidon.core.util;


import com.voxlearning.poseidon.core.exceptions.UtilException;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.lang.SimpleCache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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
            superClazz = superClass ? superClazz.getSuperclass() : null;
        }
        return allField;
    }

    //------------------------------method------------------------------------------------------------------------------

    /**
     * 返回方法名称集合
     *
     * @param clazz 　类
     * @return 方法名称集合
     */
    public static Set<String> getMethodNames(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        Method[] methods = getMethods(clazz);
        return Stream.of(methods).map(Method::getName).collect(Collectors.toSet());
    }

    /**
     * 返回符合过滤器条件的方法名称集合
     *
     * @param clazz     　类
     * @param predicate 过滤器
     * @return 方法名称集合
     */
    public static Set<String> getMethodNames(Class<?> clazz, Predicate<Method> predicate) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        Method[] methods = getMethods(clazz);
        return Stream.of(methods).filter(predicate).
                map(Method::getName).collect(Collectors.toSet());
    }

    /**
     * 返回改对象的所有方法，包括非<code>Public</code>,从父对象而来的方法
     *
     * @param obj        　对象
     * @param methodName 　方法名
     * @param args       　方法参数
     * @return
     */
    public static Method getMethodsOfObj(Object obj, String methodName, Object... args) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
    }

    /**
     * 忽略大小写返回指定的方法
     *
     * @param clazz      类
     * @param methodName 　方法名称
     * @param paramTypes 　方法参数类型　数组
     * @return 方法
     */
    public static Method getMethodsIgnoreCase(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        return getMethod(clazz, true, methodName, paramTypes);
    }

    /**
     * 返回指定的方法,不忽略大小写
     *
     * @param clazz      类
     * @param methodName 　方法名称
     * @param paramTypes 　方法参数类型　数组
     * @return 方法
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        return getMethod(clazz, false, methodName, paramTypes);
    }

    /**
     * 获取指定的方法 如果不存在返回<code>null</code>
     *
     * @param clazz      类 如果为{@code null} 返回{@code null}
     * @param ignoreCase 是否忽略大小写
     * @param methodName 　方法名称
     * @param paramTypes 　方法的参数类型
     * @return 符合条件的Method
     */
    public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) {
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

    /**
     * 判断一个方法是否为equals方法
     *
     * @param method
     * @return true or false
     */
    public static boolean isEqualsMethod(Method method) {
        if (Objects.isNull(method) || !"equals".equals(method.getName())) {
            return false;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 1 && parameterTypes[0] == Object.class;
    }

    /**
     * 判断一个方法是否为hashCode方法
     *
     * @param method 方法
     * @return true or false
     */
    public static boolean isHashCodeMethod(Method method) {
        if (Objects.isNull(method) || !"hashCode".equals(method.getName())) {
            return false;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Objects.isNull(parameterTypes) || parameterTypes.length == 0;
    }

    /**
     * 判断一个方法是否为toString方法
     *
     * @param method
     * @return
     */
    public static boolean isToStringMethod(Method method) {
        if (Objects.isNull(method) || !"toString".equals(method)) {
            return false;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Objects.isNull(parameterTypes) || parameterTypes.length == 0;
    }

    //-----------------------------instance-----------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new UtilException(e, "Instance class %s error!", clazz);
        }
    }

    /**
     * @param clazz  类
     * @param params 　构造函数参数
     * @param <T>    　泛型类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Object... params) {
        if (ArrayUtil.isEmpty(params)) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new UtilException(e, "Instance class %s error!", clazz);
            }
        }
        Constructor<T> constructor = getConstructor(clazz, ClassUtil.getClasses(params));
        if (constructor == null) {
            throw new UtilException("class [%s] have no this params [%s] type constructor", clazz.getName(), ArrayUtil.arrayToString(params));
        }
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new UtilException(e, "invoke class [%s] constructor failed.", clazz.getName());
        }
    }

    /**
     * 遍历改类的所有构造函数　创建对象
     *
     * @param clazz 类
     * @param <T>   　泛型类型
     * @return 对象实例
     */
    public static <T> T newInstanceIfPossible(Class<T> clazz) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        Constructor<T>[] constructors = getConstructor(clazz);
        Class<?>[] parameterTypes;
        for (Constructor<T> constructor : constructors) {
            parameterTypes = constructor.getParameterTypes();
            if (Objects.isNull(parameterTypes) || parameterTypes.length == 0) {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                return constructor.newInstance(ClassUtil.getDefaultValue(parameterTypes));
            } catch (Exception e) {
            }
        }
        return null;
    }

    //---------------------------------------invoke---------------------------------------------------------------------

    /**
     * 执行静态方法方法
     *
     * @param method 方法对象
     * @param args   　参数
     * @param <T>    　类型
     * @return 执行结果
     */
    public static <T> T invokeStatic(Method method, Object... args) {
        return invoke(null, method, args);
    }

    /**
     * 执行静态方法方法
     *
     * @param clazz      类型
     * @param methodName 执行方法名称
     * @param args       　参数
     * @param <T>        　类型
     * @return 结果
     */
    public static <T> T invokeStatic(Class<?> clazz, String methodName, Object... args) {
        Method methodsOfObj = getMethod(clazz, methodName, ClassUtil.getClasses(args));
        if (Objects.isNull(methodsOfObj)) {
            throw new UtilException("No such static method %s", methodName);
        }
        return invoke(null, methodsOfObj, args);
    }


    /**
     * 执行对象的某个方法
     *
     * @param obj        对象
     * @param methodName 　执行方法名称
     * @param args       　参数
     * @param <T>        　类型
     * @return result
     */
    public static <T> T invoke(Object obj, String methodName, Object... args) {
        Method md = getMethodsOfObj(obj, methodName, args);
        if (Objects.isNull(md)) {
            throw new UtilException("No such method %s", methodName);
        }
        return invoke(obj, methodName, args);
    }

    /**
     * @param obj    对象
     * @param method 　方法
     * @param args   　参数
     * @param <T>    　对象类型
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj, Method method, Object... args) {
        if (Objects.isNull(method)) {
            return null;
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        try {
            return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, args);
        } catch (Exception e) {
            throw new UtilException(e, "obj[%s] method:[%s] args[%s] invoke failed", obj.getClass().toString(),
                    method.getName(), ArrayUtil.arrayToString(args));
        }
    }

    public static void main(String[] args) {
        System.out.println(ReflectUtil.getConstructor(SimpleCache.class));
    }
}
