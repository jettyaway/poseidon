package com.voxlearning.poseidon.core.bean;


import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * Bean异常
 *
 * @author xiaoleilu
 */
public class BeanException extends RuntimeException {
    private static final long serialVersionUID = -8096998667745023423L;

    public BeanException(Throwable e) {
        super(e);
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public BeanException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BeanException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}
