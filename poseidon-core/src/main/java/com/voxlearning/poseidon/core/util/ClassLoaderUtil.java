package com.voxlearning.poseidon.core.util;

import com.voxlearning.poseidon.core.convert.BasicType;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.lang.SimpleCache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link ClassLoader} util tool suit
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/25
 * @since 2017/11/25
 */
public class ClassLoaderUtil {
    /**
     * 数组类的结尾符: "[]"
     */
    private static final String ARRAY_SUFFIX = "[]";
    /**
     * 内部数组类名前缀: "["
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[";
    /**
     * 内部非原始类型类名前缀: "[L"
     */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
    /**
     * 包名分界符: '.'
     */
    private static final char PACKAGE_SEPARATOR = StrUtil.C_DOT;
    /**
     * 内部类分界符: '$'
     */
    private static final char INNER_CLASS_SEPARATOR = '$';

    /**
     * 原始类型名和其class对应表，例如：int =》 int.class
     */
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new ConcurrentHashMap<String, Class<?>>(32);
    private static SimpleCache<String, Class<?>> classCache = new SimpleCache<>();

    static {
        List<Class<?>> primitiveTypes = new ArrayList<>(32);
        primitiveTypes.addAll(BasicType.primitiveWrapperMap.keySet());
        primitiveTypes.add(boolean[].class);
        primitiveTypes.add(byte[].class);
        primitiveTypes.add(char[].class);
        primitiveTypes.add(short[].class);
        primitiveTypes.add(int[].class);
        primitiveTypes.add(long[].class);
        primitiveTypes.add(float[].class);
        primitiveTypes.add(double[].class);
        PRIMITIVE_TYPE_NAME_MAP.putAll(primitiveTypes.stream().collect(Collectors.toMap(Class::getName, Function.identity())));
    }

    /**
     * 获取当前线程的类加载器
     *
     * @return ClassLoader
     */
    public static ClassLoader getClassContextLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取类类加载器
     * 顺序如下:<br/>
     * <pre>
     *     1.获取当前线程的ContextClassLoader
     *     2.获取{@link ClassLoaderUtil}对应的ClassLoader
     *     3.获取系统的ClassLoader
     * </pre>
     *
     * @return ClassLoader
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classContextLoader = getClassContextLoader();
        if (Objects.nonNull(classContextLoader)) {
            return classContextLoader;
        }
        classContextLoader = ClassLoaderUtil.class.getClassLoader();
        if (Objects.nonNull(classContextLoader)) {
            return classContextLoader;
        }
        return ClassLoader.getSystemClassLoader();
    }

    public static Class<?> loadClass(String name, ClassLoader classLoader, boolean isInitialized) {
        Preconditions.checkNotNull(name, "name must not be null.");
        //基本类型
        Class<?> clazz = loadPrimitiveClass(name);
        if (Objects.isNull(clazz)) {
            clazz = classCache.get(name);
        }
        if (Objects.nonNull(clazz)) {
            return clazz;
        }
        //数组类型
        String elementClassName;
        Class<?> elementClass;
        if (name.endsWith(ARRAY_SUFFIX)) {
            elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            clazz = getClass(elementClassName, classLoader, isInitialized);
        } else if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            elementClassName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            clazz = getClass(elementClassName, classLoader, isInitialized);
        } else if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            // "[[I" 或 "[[Ljava.lang.String;" 风格
            elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            clazz = getClass(elementClassName, classLoader, isInitialized);
        }
        if (Objects.nonNull(clazz)) {
            return clazz;
        }
        //普通类型
        if (Objects.isNull(classLoader)) {
            classLoader = getClassLoader();
        }
        try {
            clazz = Class.forName(name, isInitialized, classLoader);
        } catch (ClassNotFoundException e) {
            //尝试获取内部类 例如java.lang.Thread.State =》java.lang.Thread$State
            int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
            e.printStackTrace();
        }
        return clazz;
    }

    private static Class<?> getClass(String name, ClassLoader loader, boolean isInitialized) {
        Class<?> eleClazz = loadClass(name, loader, isInitialized);
        return Array.newInstance(eleClazz, 0).getClass();
    }

    private static Class<?> loadPrimitiveClass(String name) {
        if (StrUtil.isBlank(name) || name.trim().length() > 8) {
            return null;
        }
        return PRIMITIVE_TYPE_NAME_MAP.get(name.trim());
    }

}
