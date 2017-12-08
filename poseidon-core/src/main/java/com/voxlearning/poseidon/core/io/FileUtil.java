package com.voxlearning.poseidon.core.io;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.file.FileWriter;
import com.voxlearning.poseidon.core.io.resources.ResourcesUtil;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.ClassUtil;
import com.voxlearning.poseidon.core.util.CollectionUtil;
import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.core.util.URLUtil;

import java.io.BufferedWriter;
import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class FileUtil {

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = StrUtil.C_SLASH;
    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = StrUtil.C_BACKSLASH;

    /**
     * Class文件扩展名
     */
    public static final String CLASS_EXT = ".class";
    /**
     * Jar文件扩展名
     */
    public static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    public static final String JAR_PATH_EXT = ".jar!";
    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    public static final String PATH_FILE_PRE = URLUtil.FILE_URL_PREFIX;


    /**
     * 获取路径的最后一个元素
     *
     * @param path 路径
     * @return 路径的Optional对象
     */
    public static Optional<Path> getLastPathElement(Path path) {
        return getPathElement(path, -1);
    }

    /**
     * 获取指定位置的子路径部分，支持负数，-1表示从最后开始的第一个节点位置
     *
     * @param path  路径
     * @param index 路径节点的位置
     * @return 路径的Optional对象
     */
    public static Optional<Path> getPathElement(Path path, int index) {
        return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
    }


    /**
     * 获取指定位置的子路径部分，支持负数，-1表示从最后开始的第一个节点位置
     *
     * @param path      路径
     * @param fromIndex 起始节点【contains】
     * @param toIndex   结束节点【except】
     * @return 路径的Optional对象
     */
    public static Optional<Path> subPath(Path path, int fromIndex, int toIndex) {
        if (Objects.isNull(path)) {
            return Optional.empty();
        }
        int len = path.getNameCount();
        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }

        } else if (fromIndex > len) {
            fromIndex = len;
        }
        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }
        if (fromIndex > toIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (fromIndex == toIndex) {
            return Optional.empty();
        }
        return Optional.of(path.subpath(fromIndex, toIndex));
    }


    /**
     * 是否为绝对路径
     *
     * @param path 路径
     * @return true:是 false:否
     */
    public static boolean isAbsolutePath(String path) {
        if (StrUtil.isEmpty(path)) {
            return false;
        }
        //file://xxx/xxx
        if (StrUtil.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:/.*")) {
            return true;
        }
        return false;
    }

    public static File file(URL url) {
        return new File(URLUtil.toURI(url));
    }

    public static File file(String path) {
        if (StrUtil.isBlank(path)) {
            throw new NullPointerException("path can not be null or empty.");
        }
        return new File(getAbsolutePath(path));
    }

    public static File file(File parent, String child) {
        if (StrUtil.isBlank(child)) {
            throw new NullPointerException("File path is null.");
        }
        return new File(parent, child);
    }

    public static File file(String parent, String child) {
        if (StrUtil.isBlank(child)) {
            throw new NullPointerException("File path is null.");
        }
        return new File(parent, child);
    }

    /**
     * 获取绝对路径，相对于ClassPath的目录<br>
     * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/<br>
     * 兼容Spring风格的路径表示，例如：classpath:config/example.setting也会被识别后转换
     *
     * @param path
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        return getAbsolutePath(path, null);
    }

    /**
     * 获取标准的绝对路径
     *
     * @param file 文件对象
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * 获取相对于ClassPath的绝对路径
     * 该方法不会判断路径是否有效　即是否存在
     * 　如果该文件不存在那么就返回当前ClassPath+path,构造一个绝对路径但是此文件不存在
     *
     * @param path  　相对路径
     * @param clazz 　相对路径相对的类
     * @return 转化后的路径
     */
    public static String getAbsolutePath(String path, Class<?> clazz) {
        if (Objects.isNull(path)) {
            path = StrUtil.EMPTY;
        } else {
            path = normalize(path);
            if (isAbsolutePath(path)) {
                return path;
            }
        }

        //相对于ClassPath路径
        URL url = ResourcesUtil.getResource(path, clazz);
        if (Objects.nonNull(url)) {
            return FileUtil.normalize(URLUtil.getDecodedPath(url));
        }
        //如果资源不存在，则返回一个拼接的资源绝对路径
        String classPath = ClassUtil.getClassPath();
        if (Objects.isNull(classPath)) {
            throw new NullPointerException("class path is null");
        }
        return classPath.concat(path);
    }

    /**
     * 修复路径<br>
     * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
     * <ol>
     * <li>1. 统一用 /</li>
     * <li>2. 多个 / 转换为一个 /</li>
     * <li>3. 去除两边空格</li>
     * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
     * </ol>
     * <p>
     * 栗子：
     * <pre>
     * "/foo//" =》 "/foo/"
     * "/foo/./" =》 "/foo/"
     * "/foo/../bar" =》 "/bar"
     * "/foo/../bar/" =》 "/bar/"
     * "/foo/../bar/../baz" =》 "/baz"
     * "/../" =》 "/"
     * "foo/bar/.." =》 "foo"
     * "foo/../bar" =》 "bar"
     * "foo/../../bar" =》 "bar"
     * "//server/foo/../bar" =》 "/server/bar"
     * "//server/../bar" =》 "/bar"
     * "C:\\foo\\..\\bar" =》 "C:/bar"
     * "C:\\..\\bar" =》 "C:/bar"
     * "~/foo/../bar/" =》 "~/bar/"
     * "~/../bar" =》 "bar"
     * </pre>
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        }

        // 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
        String pathToUse = StrUtil.removePrefixIgnoreCase(path, "classpath:");
        //去除file:前缀
        pathToUse = StrUtil.removePrefixIgnoreCase(pathToUse, "file:");
        //统一使用斜杠
        pathToUse = pathToUse.replaceAll("[/\\\\]{1,}", "/").trim();

        int prefixIndex = pathToUse.indexOf(StrUtil.COLON);
        String prefix = "";
        if (prefixIndex > -1) {
            //可能Windows风格路径
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (StrUtil.startWith(prefix, StrUtil.C_SLASH)) {
                //去除类似于/C:这类路径开头的斜杠
                prefix = prefix.substring(1);
            }
            if (!prefix.contains("/")) {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            } else {
                //如果前缀中包含/,说明非Windows风格path
                prefix = StrUtil.EMPTY;
            }
        }
        if (pathToUse.startsWith(StrUtil.SLASH)) {
            prefix += StrUtil.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = StrUtil.split(pathToUse, StrUtil.C_SLASH);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        String element;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            element = pathList.get(i);
            if (StrUtil.DOT.equals(element)) {
                // 当前目录，丢弃
            } else if (StrUtil.DOUBLE_DOT.equals(element)) {
                tops++;
            } else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }
        return prefix + CollectionUtil.join(pathElements, StrUtil.SLASH);
    }

    /**
     * 可读文件大小
     *
     * @param size 类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size < 0) {
            return "0";
        }
        String[] unit = new String[]{"B", "KB", "MB", "GB", "TB", "EB"};
        int digit = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digit)) + " " + unit[digit];
    }

    /**
     * 创建文件,不存在时新建
     *
     * @param file 文件
     * @return 文件
     */
    public static File touch(File file) {
        Preconditions.checkNotNull(file);
        if (file.exists()) {
            return file;
        }
        mkdirParentDirs(file);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return file;
    }

    public static File touch(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        File file = file(filePath);
        return touch(file);
    }

    public static File touch(File parent, String child) {
        return touch(file(parent, child));
    }

    public static File touch(String parent, String child) {
        return touch(file(parent, child));
    }


    /**
     * 创建父目录
     *
     * @param file 文件
     * @return 文件
     */
    public static File mkdirParentDirs(File file) {
        File parentFile = file.getParentFile();
        if (parentFile != null || !parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile;
    }

    /**
     * 创建父目录
     *
     * @param filePath 文件
     * @return 文件
     */
    public static File mkdirParentDirs(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        return mkdirParentDirs(file(filePath));
    }

    /**
     * 创建目录
     *
     * @param file 目录
     * @return 创建的目录
     */
    public static File mkdirs(File file) {
        if (Objects.isNull(file)) {
            return null;
        }
        if (file.exists()) {
            return file;
        }
        file.mkdirs();
        return file;
    }

    public static File mkdirs(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        return mkdirs(file(filePath));
    }

    /**
     * 获取bufferedWriter
     *
     * @param file    文件
     * @param charset 编码
     * @param append  是否追加
     * @return {@link BufferedWriter}
     */
    public static BufferedWriter getWriter(File file, Charset charset, boolean append) {
        return FileWriter.create(file, charset).getWriter(append);
    }

    public static BufferedWriter getWrite(String filePath, Charset charset, boolean append) {
        return getWriter(touch(filePath), charset, append);
    }

    public static void main(String[] args) {
        File file = new File("/home/suhao/Desktop", "8080");
        System.out.println(file.getPath());
    }
}
