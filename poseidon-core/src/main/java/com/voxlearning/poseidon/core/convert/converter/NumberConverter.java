package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.AbstractConvert;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public class NumberConverter extends AbstractConvert<Number> {

    private Class<? extends Number> targetType;

    public NumberConverter() {
        this.targetType = Number.class;
    }

    public NumberConverter(Class<? extends Number> targetType) {
        this.targetType = targetType;
    }


    @Override
    protected Number convertInternal(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        NumberEnum numberEnumByClazz = NumberEnum.fromClass(targetType);
        if (Objects.isNull(numberEnumByClazz)) {
            throw new UnsupportedOperationException(StrUtil.format("UnSupport Number type [%s]", this.targetType.getName()));
        }
        return numberEnumByClazz.getValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Number> getTargetType() {
        return (Class<Number>) this.targetType;
    }
}
