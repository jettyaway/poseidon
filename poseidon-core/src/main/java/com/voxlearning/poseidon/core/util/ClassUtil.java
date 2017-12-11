package com.voxlearning.poseidon.core.util;


import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.io.resources.ResourcesUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 类型工具类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class ClassUtil {


    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T obj) {
        return Objects.isNull(obj) ? null : (Class<T>) obj.getClass();
    }

    /**
     * 检查from 和to 两个数组中的类型在相同位置是否相同，或者来自父类或者接口
     *
     * @param from 比较类型1
     * @param to   比较类型2
     * @return 是否相同，父类或者接口
     */
    public static boolean isAllAssignablFrom(Class<?>[] from, Class<?>[] to) {
        if (ArrayUtil.isEmpty(from) && ArrayUtil.isEmpty(to)) {
            return true;
        }
        if (ArrayUtil.isEmpty(from) || ArrayUtil.isEmpty(to)) {
            return false;
        }
        if (from.length == to.length) {
            return false;
        }
        for (int i = 0; i < from.length; i++) {
            if (!from[i].isAssignableFrom(to[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回参数所代表的类型　如果参数为<code>null</code> 则返回<code>null</code>
     *
     * @param args 参数列表
     * @return 类型数组
     */
    public static Class<?>[] getClasses(Object... args) {
        return Stream.of(args).map(arg -> {
            if (Objects.isNull(arg)) {
                return Object.class;
            }
            return arg.getClass();
        }).toArray(Class[]::new);
    }

    /**
     * 获取一组类型的默认值
     *
     * @param classes 　类型数组
     * @return object 数组
     */
    public static Object[] getDefaultValue(Class<?>[] classes) {
        return Stream.of(classes).map(ClassUtil::getDefaultValue).toArray(Object[]::new);
    }

    /**
     * 返回基本类的默认值，其他类型返回<code>null</code>
     *
     * @param clazz 类
     * @return object
     */
    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (char.class == clazz) {
                return (char) 0;
            } else if (byte.class == clazz) {
                return (byte) 0;
            } else if (short.class == clazz) {
                return (short) 0;
            } else if (int.class == clazz) {
                return 0;
            } else if (float.class == clazz) {
                return 0.0F;
            } else if (long.class == clazz) {
                return 0L;
            } else if (double.class == clazz) {
                return 0.0D;
            } else if (boolean.class == clazz) {
                return false;
            }
        }
        return null;
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 获取classPath路径
     *
     * @return
     */
    public static String getClassPath() {
        return getClasspath(false);
    }

    /**
     * 获取classpath
     *
     * @param isEncode 路径中是否包含中文
     * @return classpath
     */
    public static String getClasspath(boolean isEncode) {
        URL classPathURL = getClassPathURL();
        String url = isEncode ? classPathURL.getPath() : URLUtil.getDecodedPath(classPathURL);
        return FileUtil.normalize(url);
    }

    public static URL getClassPathURL() {
        return getResourceURL(StrUtil.EMPTY);
    }

    public static URL getResourceURL(String resource) {
        return ResourcesUtil.getResource(resource);
    }

    public static URL getResourceURL(String resource, Class<?> baseClass) {
        return ResourcesUtil.getResource(resource, baseClass);
    }

    public static Class<?> getTypeArgument(Class<?> clazz) {
        return getTypeArgument(clazz, 0);
    }



    /**
     * 获得执行类型的指定位置的泛型参数类型
     *
     * @param clazz 　指定类型
     * @param index 　泛型参数索引
     * @return {@link Class}
     */
    public static Class<?> getTypeArgument(Class<?> clazz, int index) {
        Type typeArgument = TypeUtil.getTypeArgument(clazz, index);
        if (Objects.nonNull(typeArgument) && typeArgument instanceof Class) {
            return (Class<?>) typeArgument;
        }
        return null;
    }

    /**
     * 获取类名
     *
     * @param obj      获取类名对象
     * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
     * @return 类名
     */
    public static String getClassName(Object obj, boolean isSimple) {
        if (null == obj) {
            return null;
        }
        final Class<?> clazz = obj.getClass();
        return getClassName(clazz, isSimple);
    }

    /**
     * 获取类名<br>
     * 类名并不包含“.class”这个扩展名<br>
     * 例如：ClassUtil这个类<br>
     * <p>
     * <pre>
     * isSimple为false: "com.xiaoleilu.hutool.util.ClassUtil"
     * isSimple为true: "ClassUtil"
     * </pre>
     *
     * @param clazz    类
     * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
     * @return 类名
     */
    public static String getClassName(Class<?> clazz, boolean isSimple) {
        if (null == clazz) {
            return null;
        }
        return isSimple ? clazz.getSimpleName() : clazz.getName();
    }

    /**
     * 是否为标准的类<br>
     * 这个类必须：
     * <pre>
     * 1、非接口
     * 2、非抽象类
     * 3、非Enum枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long等）
     * </pre>
     *
     * @param clazz 类
     * @return 是否为标准类
     */
    public static boolean isNormalClass(Class<?> clazz) {
        return null != clazz
                && !clazz.isInterface()
                && !isAbstract(clazz)
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isAnnotation()
                && !clazz.isSynthetic()
                && !clazz.isPrimitive();
    }

    /**
     * 是否为抽象类
     *
     * @param clazz 类
     * @return 是否为抽象类
     */
    public static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 查找类中所有的字段包括非Public 字段
     *
     * @param clazz 类型
     * @return 字段数组
     * @throws SecurityException 安全异常{@link SecurityException}
     */
    public static Field[] getDeclaredFields(Class<?> clazz) throws SecurityException {
        if (Objects.isNull(clazz)) {
            return null;
        }
        return clazz.getDeclaredFields();
    }

    public static Field getDeclaredField(Class<?> clazz, String filedName) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        if (StrUtil.isBlank(filedName)) {
            return null;
        }
        Field[] declaredFields = getDeclaredFields(clazz);
        try {
            return clazz.getDeclaredField(filedName);
        } catch (NoSuchFieldException e) {
        }
        return null;
    }

    public static Method setAccessible(Method method) {
        if (Objects.nonNull(method) || !method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }
}
