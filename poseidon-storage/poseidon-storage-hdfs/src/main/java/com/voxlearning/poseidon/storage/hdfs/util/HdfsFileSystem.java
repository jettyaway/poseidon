package com.voxlearning.poseidon.storage.hdfs.util;

import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.storage.hdfs.exception.HdfsRuntimeException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;


/**
 * HDFS 工具类
 *
 * @author hao.su
 * @date 2017-12-29
 */
public class HdfsFileSystem {

    private static Logger logger = Logger.getLogger(HdfsFileSystem.class);

    /**
     * 按路径上传文件到hdfs
     *
     * @param local  本地
     * @param remote 远程
     * @throws IOException 抛出io异常
     */
    public static void copyFile(String local, String remote) throws IOException {
        Path remotePath = new Path(remote);
        FileSystem fs = getFileSystem(remotePath);
        if (!fs.exists(remotePath)) {
            fs.mkdirs(remotePath, new FsPermission("777"));
        }
        fs.copyFromLocalFile(new Path(local), remotePath);
        logger.info("copy from: " + local + " to " + remote);
        fs.close();
    }

    /**
     * 按路径下载hdfs上的文件
     *
     * @param conf   配置
     * @param uri    uri
     * @param remote 远程
     * @param local  本地
     * @throws IOException 抛出io异常
     */
    public static void download(Configuration conf, String uri, String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.copyToLocalFile(path, new Path(local));
        logger.info("download: from" + remote + " to " + local);
        fs.close();
    }

    /**
     * File对象上传到hdfs
     *
     * @param localPath 本地路径
     * @param hdfsPath  hdfs路径
     * @throws IOException io异常
     */
    public static void createFile(File localPath, String hdfsPath) throws IOException {
        InputStream in = null;
        try {
            Configuration conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
            FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
            in = new BufferedInputStream(new FileInputStream(localPath));
            IOUtils.copyBytes(in, out, 4096, false);
            out.hsync();
            out.close();
            logger.info("create file in hdfs:" + hdfsPath);
        } finally {
            IOUtils.closeStream(in);
        }
    }


    /**
     * 从输入流中创建hdfs文件
     * 关闭
     *
     * @param inputStream 输入流
     * @param hdfsPath    hdfs路径
     */
    public static void createFile(InputStream inputStream, String hdfsPath) throws IOException {
        InputStream in = null;
        try {
            Configuration conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
            FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
            in = new BufferedInputStream(inputStream);
            IOUtils.copyBytes(in, out, 4096, false);
            out.hsync();
            out.close();
            logger.info("create file in hdfs:" + hdfsPath);
        } finally {
            IOUtils.closeStream(in);
        }
    }

    /**
     * 通过流向文件中写
     * 不关闭流
     *
     * @param inputStream  输入流
     * @param outputStream hdfs输出流
     * @throws IOException
     */
    public static void write(InputStream inputStream, FSDataOutputStream outputStream) throws IOException {
        IOUtils.copyBytes(inputStream, outputStream, 2048, false);
        //outputStream.hsync();
    }

    /**
     * 文件中写
     * 不关闭流
     * @param bytes
     * @param outputStream
     * @throws IOException
     */
    public static void write(byte[] bytes, FSDataOutputStream outputStream) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            IOUtils.copyBytes(inputStream, outputStream, 2048, false);
        }
        //outputStream.hsync();
    }

    /**
     * 非递归的删除hdfs上的文件(非目录)
     *
     * @param path hdfs file path
     * @throws IOException io exception
     */
    public static boolean deleteFile(String path) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "path can not be null or empty");
        if (StrUtil.SLASH.equals(path)) {
            logger.error("[/] path is illegal");
            return false;
        }
        Path fsPath = new Path(path);
        FileSystem fileSystem = getFileSystem(fsPath);
        if (!fileSystem.exists(fsPath)) {
            logger.info("path:" + path + ",does not exits");
            return true;
        }
        boolean flag = false;
        if (fileSystem.isFile(fsPath)) {
            logger.info("delete hdfs file:" + path);
            flag = fileSystem.delete(fsPath, false);
        }
        return flag;
    }

    /**
     * 判断hdfs路径是否存在
     *
     * @param path hdfs路径
     * @return true or false
     */
    public static boolean exist(String path, FileSystem fileSystem) throws IOException {
        return fileSystem.exists(new Path(path));
    }


    /**
     * 列出指定hdfs路径下的所有文件的名字
     *
     * @param dir hdfs路径
     * @return List<String>
     */
    public static List<String> listFiles(String dir, FileSystem fileSystem) throws IOException {
        List<String> pathList = null;
        if (!exist(dir, fileSystem)) {
            return null;
        }
        try {
            Path path = new Path(dir);
            fileSystem = getFileSystem(path);
            FileStatus[] fileStatuses = fileSystem.listStatus(path);
            pathList = Stream.of(fileStatuses).filter(it -> it.isFile() || it.isDirectory())
                    .map(it -> it.getPath().getName()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.isNull(pathList) ? Collections.EMPTY_LIST : pathList;
    }

    /**
     * 覆盖创建文件
     *
     * @param inputStream 输入流
     * @param filePath    文件路径
     * @throws IOException 异常
     */
    public static void createFileCorver(InputStream inputStream, String filePath, FileSystem fileSystem) throws IOException {
        Preconditions.checkArgument(Objects.nonNull(inputStream) && !Strings.isNullOrEmpty(filePath));
        if (exist(filePath, fileSystem) && isFile(filePath, fileSystem)) {
            deleteFile(filePath);
        }
        createFile(inputStream, filePath);
    }

    public static FileSystem getFileSystem(Path path) throws IOException {
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);
        return path.getFileSystem(conf);
    }

    public static FSDataOutputStream getFSDataOutputStream(String filePath, FileSystem fileSystem) throws IOException {
        Preconditions.checkArgument(StrUtil.isNotBlank(filePath));
        return fileSystem.create(new Path(filePath));
    }

    public static boolean isFile(String path, FileSystem fileSystem) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path));
        boolean isFile;
        try {
            Path fsPath = new Path(path);
            isFile = fileSystem.isFile(fsPath);
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
        return isFile;
    }

    public static void rename(String sourcePath, String targetPath, final FileSystem fs) throws IOException {
        if (Objects.equals(sourcePath, targetPath)) {
            return;
        }
        if (Objects.isNull(fs)) {
            throw new HdfsRuntimeException("fs cant not be null.");
        }
        final Path srcPath = new Path(sourcePath);
        final Path dstPath = new Path(targetPath);
        fs.rename(srcPath, dstPath);
    }
}
