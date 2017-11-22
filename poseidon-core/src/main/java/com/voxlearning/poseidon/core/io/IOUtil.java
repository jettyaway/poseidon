package com.voxlearning.poseidon.core.io;

import java.io.Closeable;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class IOUtil {

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }
}
