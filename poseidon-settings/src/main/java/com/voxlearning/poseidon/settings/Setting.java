package com.voxlearning.poseidon.settings;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Setting 工具类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-11
 * @since 17-12-11
 */
public class Setting extends BasicSetting {

    private static final long serialVersionUID = 1659921755006682313L;

    public Setting() {
    }

    public Setting(String path) {
        super(path);
    }

    public Setting(String pathBaseClassLoader, Charset charset, boolean isUseVariable) {
        super(pathBaseClassLoader, charset, isUseVariable);
    }

    public Setting(String pathBaseClassLoader, Class<?> classLoader, Charset charset, boolean isUseVariable) {
        super(pathBaseClassLoader, charset, isUseVariable);
    }

    public Setting(String pathBaseClassLoader, boolean isUseVariable) {
        super(pathBaseClassLoader, DEFAULT_CHARSET, isUseVariable);
    }

    public Setting(File file, Charset charset, boolean isUseVariable) {
        super(file, charset, isUseVariable);
    }

    public Setting(URL url, Charset charset, boolean isUseVariable) {
        super(url, charset, isUseVariable);
    }
}
