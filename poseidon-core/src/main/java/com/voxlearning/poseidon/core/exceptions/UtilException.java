package com.voxlearning.poseidon.core.exceptions;


import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * 工具类异常
 *
 * @author xiaoleilu
 */
public class UtilException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UtilException(Throwable e, String template, Object... args) {
        super(StrUtil.format(template, args), e);
    }

}
