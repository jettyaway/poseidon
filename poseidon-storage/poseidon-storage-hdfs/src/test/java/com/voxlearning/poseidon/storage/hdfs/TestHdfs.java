package com.voxlearning.poseidon.storage.hdfs;


import com.voxlearning.poseidon.storage.hdfs.util.HdfsFileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-27
 * @since 17-12-27
 */
public class TestHdfs {

    private FileSystem fileSystem = null;
    private FSDataOutputStream out = null;
    private Path path;

    @Before
    public void init() {
        path = new Path("hdfs://M7-10-6-0-87:8020/upload/hao.su/test.text");
        try {
            fileSystem = HdfsFileSystem.getFileSystem(path);
            out = fileSystem.create(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrite() {
        String test = "HELLO WORLD\n";
        ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes());
        try {
            HdfsFileSystem.write(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        in = new ByteArrayInputStream(test.getBytes());
        try {
            HdfsFileSystem.write(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAppend() {
        try {
            out = fileSystem.append(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String test = "my name is suhao\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(test.getBytes());
        try {
            HdfsFileSystem.write(inputStream,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void destory() {

    }


}
