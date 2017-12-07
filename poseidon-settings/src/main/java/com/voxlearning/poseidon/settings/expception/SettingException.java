package com.voxlearning.poseidon.settings.expception;

import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * 设置异常类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-07
 * @since 17-12-7
 */
public class SettingException extends Exception {

    private static final long serialVersionUID = 4890682290437015394L;

    public SettingException(Throwable e) {
        super(e);
    }

    public SettingException(String message) {
        super(message);
    }

    public SettingException(String template, Object... args) {
        super(StrUtil.format(template, args));
    }

    public SettingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SettingException(Throwable throwable, String template, Object... args) {
        super(StrUtil.format(template, args), throwable);
    }
}
