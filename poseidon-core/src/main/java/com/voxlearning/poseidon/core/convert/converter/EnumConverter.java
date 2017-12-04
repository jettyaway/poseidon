package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.AbstractConvert;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/12/4
 * @since 2017/12/4
 */
public class EnumConverter<E extends Enum<E>> extends AbstractConvert<E> {

    private Class<E> clazz;

    public EnumConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected E convertInternal(Object value) {
        return Enum.valueOf(clazz, convertToString(value));
    }
}
