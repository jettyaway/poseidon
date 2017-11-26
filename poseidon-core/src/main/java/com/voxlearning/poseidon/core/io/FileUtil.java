package com.voxlearning.poseidon.core.io;

import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.core.util.URLUtil;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

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

}
