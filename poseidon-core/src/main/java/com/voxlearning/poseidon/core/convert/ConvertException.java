package com.voxlearning.poseidon.core.convert;

import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * 转化异常类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-28
 * @since 17-11-28
 */
public class ConvertException extends RuntimeException {

    public ConvertException() {
        super();
    }

    public ConvertException(Throwable e) {
        super(e);
    }

    public ConvertException(String template, Object... args) {
        super(StrUtil.format(template, args));
    }

    public ConvertException(Throwable e, String template, Object... args) {
        super(StrUtil.format(template, args), e);
    }

}
