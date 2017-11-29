package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.AbstractConvert;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public class BooleanConvert extends AbstractConvert<Boolean> {

    @Override
    protected Boolean convertInternal(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String strValue = convertToString(value);
        return Boolean.valueOf(PrimitiveEnum.parseBoolean(strValue));
    }
}
