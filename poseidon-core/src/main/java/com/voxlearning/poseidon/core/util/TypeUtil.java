package com.voxlearning.poseidon.core.util;

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
}
