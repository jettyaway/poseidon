package com.voxlearning.poseidon.core.convert;

import com.voxlearning.poseidon.core.util.ClassUtil;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-28
 * @since 17-11-28
 */
public abstract class AbstractConvert<T> implements Converter<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T convert(Object value, T defaultValue) throws IllegalArgumentException {
        Class<T> targetType = getTargetType();
        if (Objects.isNull(targetType) && Objects.isNull(defaultValue)) {
            throw new NullPointerException(StrUtil.format("targetType and defaultValue both null ,we can not know the convert:[%s] type", getClass().getName()));
        }

        if (Objects.isNull(targetType)) {
            targetType = (Class<T>) defaultValue.getClass();
        }
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        if (Objects.isNull(defaultValue) || targetType.isInstance(defaultValue)) {
            if (targetType.isInstance(value)) {
                return (T) targetType.cast(value);
            }
            T result;
            try {
                result = convertInternal(value);
            } catch (RuntimeException e) {
                return defaultValue;
            }
            return Objects.isNull(result) ? defaultValue : result;
        }
        throw new IllegalArgumentException(StrUtil.format("defaultValue [%s] is not the instance of [%s]", defaultValue, targetType.getName()));
    }

    protected abstract T convertInternal(Object value);


    /**
     * 将任意对象转化为{@link String}
     *
     * @param value 对象
     * @return {@link String}
     */
    protected String convertToString(Object value) {
        return StrUtil.convertToString(value);
    }

    /**
     * 获取此类的实现的泛型类型
     *
     * @return {@link Class}
     */
    @SuppressWarnings("unchecked")
    public Class<T> getTargetType() {
        return (Class<T>) ClassUtil.getTypeArgument(getClass(), 0);
    }
}
