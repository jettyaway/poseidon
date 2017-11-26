package com.voxlearning.poseidon.core.util;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/26
 * @since 2017/11/26
 */
public class FileUtil {

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

    public static String getAbsolutePath(String path) {

        return null;
    }

    public static String getAbsolutePath(String path, Class<?> clazz) {
        if (Objects.isNull(path)) {
            path = StrUtil.EMPTY;
        } else {
            path = normalize(path);
        }
        //TODO impl
        return null;
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
            if (false == prefix.contains("/")) {
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

        //List<String> pathList = StrUtil.split(pathToUse, StrUtil.C_SLASH);
        List<String> pathList = Arrays.asList(pathToUse.split("/"));
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

        //return prefix + CollectionUtil.join(pathElements, StrUtil.SLASH);
        //TODO impl
        return "";
    }
}
