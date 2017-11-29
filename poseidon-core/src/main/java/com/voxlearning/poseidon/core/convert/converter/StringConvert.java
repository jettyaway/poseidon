package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.AbstractConvert;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public class StringConvert extends AbstractConvert<String> {

    @Override
    protected String convertInternal(Object value) {
        return convertToString(value);
    }
}
