package com.voxlearning.poseidon.core.io.exception;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class IoRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5841630044734882117L;

    public IoRuntimeException(Throwable e) {
        super(e.getClass().getSimpleName() + "," + e.getMessage(), e);
    }

    public IoRuntimeException(String message) {
        super(message);
    }
}
