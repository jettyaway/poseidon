package com.voxlearning.poseidon.core.io;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.lang.Preconditions;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

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

    public static String read(Reader reader) throws IORuntimeException {
        StringBuilder stringBuilder = new StringBuilder();
        CharBuffer charBuffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (reader.read(charBuffer) != -1) {
                stringBuilder.append(charBuffer.flip().toString());
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public static byte[] readBytes(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copy(inputStream, outputStream);
        return outputStream.toByteArray();
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) {
        return copy(inputStream, outputStream, bufferSize, null);
    }

    public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize, StreamProgress progress) {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        byte[] buffer = new byte[bufferSize];
        long size = 0;
        if (Objects.nonNull(progress)) {
            progress.start();
        }
        try {
            for (int readSize = -1; (readSize = inputStream.read(buffer)) != EOF; ) {
                outputStream.write(buffer, 0, readSize);
                size += readSize;
                outputStream.flush();
                if (Objects.nonNull(progress)) {
                    progress.progress(size);
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (Objects.nonNull(progress)) {
            progress.finish();
        }
        return size;
    }

}
