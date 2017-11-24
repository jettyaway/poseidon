package com.voxlearning.poseidon.core.exceptions;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-24
 * @since 17-11-24
 */
public class SerializerException extends RuntimeException {

    public SerializerException() {
        super();
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(Throwable throwable) {
        super(throwable);
    }
}
