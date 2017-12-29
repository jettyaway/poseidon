package com.voxlearning.poseidon.storage.hdfs.bucket;

import java.io.IOException;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-29
 * @since 17-12-29
 */
public interface HdfsWriter {

    void open(String filePath) throws IOException;

    void append(byte[] bytes) throws IOException;

    void append(byte[] bytes, boolean newLine) throws IOException;

    void sync() throws IOException;

    void close() throws IOException;

}
