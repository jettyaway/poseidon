package com.voxlearning.poseidon.storage.hdfs.bucket;

import com.voxlearning.poseidon.core.util.CharsetUtil;
import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.storage.hdfs.util.HdfsFileSystem;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * FSDataStream writer
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-29
 * @since 17-12-29
 */
public class HdfsDataStream extends AbstractHdfsWriter {
    private FSDataOutputStream outputStream;

    @Override
    public void open(String filePath) throws IOException {
        FileSystem fileSystem = HdfsFileSystem.getFileSystem(new Path(filePath));
        outputStream = fileSystem.create(new Path(filePath));
    }

    @Override
    public void append(byte[] bytes) throws IOException {
        HdfsFileSystem.write(bytes, outputStream);
    }

    @Override
    public void append(byte[] bytes, boolean newLine) throws IOException {
        HdfsFileSystem.write(bytes, outputStream);
        HdfsFileSystem.write(StrUtil.bytes(StrUtil.LF, CharsetUtil.CHARSET_UTF_8), outputStream);
    }

    @Override
    public void sync() throws IOException {
        outputStream.hflush();
    }

    @Override
    public void close() throws IOException {
        this.sync();
        outputStream.close();
    }
}
