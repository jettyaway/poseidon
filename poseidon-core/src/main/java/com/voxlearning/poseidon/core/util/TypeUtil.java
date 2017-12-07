package com.voxlearning.poseidon.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 类型工具
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-28
 * @since 17-11-28
 */
public class TypeUtil {

    /**
     * 获取指定类型的泛型参数
     *
     * @param type  　类型，必须是泛型类型
     * @param index 　第几个泛型
     * @return 类型
     */
    public static Type getTypeArgument(Type type, int index) {
        Type[] typeArguments = getTypeArguments(type);
        if (Objects.nonNull(typeArguments) && typeArguments.length > index) {
            return typeArguments[index];
        }
        return null;
    }

    /**
     * 获得指定类型中的所有泛型参数类型
     *
     * @param type 指定类型
     * @return 所有的泛型参数类型
     */
    public static Type[] getTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType genericSuperclass = (ParameterizedType) type;
            return genericSuperclass.getActualTypeArguments();
        }
        return null;
    }

    /**
     * 通过field获取type<br/>
     * 优先获取GenericType 其次Type
     *
     * @param field 字段
     * @return {@link Type} 可能返回{@code null}
     */
    public static Type getType(Field field) {
        if (Objects.isNull(field)) {
            return null;
        }
        Type type = field.getGenericType();
        if (Objects.isNull(type)) {
            type = field.getType();
        }
        return type;
    }

    /**
     * 通过field获取Class
     *
     * @param field 字段
     * @return {@link Class} 可能返回{@code null}
     */
    public static Class<?> getClass(Field field) {
        if (Objects.isNull(field)) {
            return null;
        }
        return field.getType();
    }

    public static Class<?> getReturnClass(Method method) {
        if (Objects.isNull(method)) {
            return null;
        }
        return method.getReturnType();
    }

    public static Type getReturnType(Method method) {
        return method.getGenericReturnType();
    }

    public static Type getFirstParamType(Method method) {
        return getParamType(method, 0);
    }

    public static Type getParamType(Method method, int index) {
        Type[] parameterTypes = method.getGenericExceptionTypes();
        if (Objects.isNull(parameterTypes) || parameterTypes.length <= 0 || index >= parameterTypes.length) {
            return null;
        }
        return parameterTypes[index];
    }

    public static Class<?> getFirstParamClass(Method method) {
        if (Objects.isNull(method)) {
            return null;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (Objects.isNull(parameterTypes) || parameterTypes.length <= 0) {
            return null;
        }
        return parameterTypes[0];
    }
}
