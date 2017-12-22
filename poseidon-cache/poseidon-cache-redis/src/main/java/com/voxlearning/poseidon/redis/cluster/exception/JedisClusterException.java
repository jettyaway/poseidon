package com.voxlearning.poseidon.redis.cluster.exception;


import com.voxlearning.poseidon.core.util.StrUtil;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-22
 * @since 17-12-22
 */
public class JedisClusterException extends RuntimeException {
    public JedisClusterException() {

    }

    public JedisClusterException(String message) {
        super(message);
    }

    public JedisClusterException(String template, Object... params) {
        super(StrUtil.format(template, params));
    }

    public JedisClusterException(Throwable tx, String message) {
        super(message, tx);
    }

    public JedisClusterException(Throwable tx, String template, Object... params) {
        super(StrUtil.format(template, params), tx);
    }
}
