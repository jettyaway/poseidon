package com.voxlearning.poseidon.core.file;

import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.util.CharsetUtil;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 文件包装类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class FileWrapper {

    protected File file;

    protected Charset charset;

    public static final String DEFAULT_CHARSET = CharsetUtil.UTF_8;

    public FileWrapper(File file, Charset charset) {
        this.charset = charset;
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String readableFileSize() {
        return FileUtil.readableFileSize(file.length());
    }

}
