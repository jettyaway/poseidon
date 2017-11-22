package com.voxlearning.poseidon.core.io.exception;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class WatchException extends RuntimeException {

    public WatchException(Throwable e) {
        super(e.getClass().getSimpleName() + "," + e.getMessage(), e);
    }

    public WatchException(String message) {
        super(message);
    }
}
