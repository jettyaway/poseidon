package com.voxlearning.poseidon.storage.hdfs.exception;

import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-29
 * @since 17-12-29
 */
public class HdfsRuntimeException extends RuntimeException {

    public HdfsRuntimeException() {

    }

    public HdfsRuntimeException(String message) {
        super(message);
    }

    public HdfsRuntimeException(String template, Object... args) {
        super(StrUtil.format(template, args));
    }

    public HdfsRuntimeException(Throwable x, String template, Object... args) {
        super(StrUtil.format(template, args), x);
    }

    public HdfsRuntimeException(Throwable x, String message) {
        super(message, x);
    }
}
