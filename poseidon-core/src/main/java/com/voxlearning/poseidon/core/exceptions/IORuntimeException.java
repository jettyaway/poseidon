package com.voxlearning.poseidon.core.exceptions;

import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/26
 * @since 2017/11/26
 */
public class IORuntimeException extends RuntimeException {

    public IORuntimeException() {
        super();
    }

    public IORuntimeException(Throwable e) {
        super(e);
    }

    public IORuntimeException(String template, Object... args) {
        super(StrUtil.format(template, args));
    }

    public IORuntimeException(Throwable e, String template, Object... args) {
        super(StrUtil.format(template, args), e);
    }
}
