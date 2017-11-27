package com.voxlearning.poseidon.settings.expception;

import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-27
 * @since 17-11-27
 */
public class SettingRuntimeException extends RuntimeException {

    public SettingRuntimeException() {
        super();
    }

    public SettingRuntimeException(Throwable e) {
        super(e);
    }

    public SettingRuntimeException(String template, Object... args) {
        super(StrUtil.format(template, args));
    }

    public SettingRuntimeException(Throwable e, String template, Object... args) {
        super(StrUtil.format(template, args), e);
    }
}
