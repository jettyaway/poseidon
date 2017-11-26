package com.voxlearning.poseidon.core.io;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class IOUtil {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 4096;
    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    public static BufferedReader getReader(InputStream inputStream) {
        return getReader(inputStream, Charset.defaultCharset());
    }

    public static BufferedReader getReader(InputStream inputStream, Charset charset) {
        return new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    public static BufferedReader getReader(InputStream inputStream, String charsetName) {
        try {
            return new BufferedReader(new InputStreamReader(inputStream, charsetName));
        } catch (UnsupportedEncodingException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedReader getReader(Reader reader) {
        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        }
        return new BufferedReader(reader);
    }
}
