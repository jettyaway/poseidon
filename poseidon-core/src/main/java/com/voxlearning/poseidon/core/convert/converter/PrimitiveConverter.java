package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.AbstractConvert;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public class PrimitiveConverter extends AbstractConvert<Object> {

    private Class<?> targetClass;

    public PrimitiveConverter(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    protected Object convertInternal(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        PrimitiveEnum primitiveEnum = PrimitiveEnum.fromClass(targetClass);
        if (Objects.isNull(primitiveEnum)) {
            throw new UnsupportedOperationException(StrUtil.format("UnSupport Number type [%s]", this.targetClass.getName()));
        }
        return primitiveEnum.getValue(value);
    }
}
