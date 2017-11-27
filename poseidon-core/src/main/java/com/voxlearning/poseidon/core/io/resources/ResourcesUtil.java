package com.voxlearning.poseidon.core.io.resources;


import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.util.ClassLoaderUtil;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * Created by hao.su
 * Created by 2017/11/25
 * History 2017/11/25.
 */
public class ResourcesUtil {

    public static Resources getResourceObj(String path) {

        return FileUtil.isAbsolutePath(path) ? new FileResource(path) : new ClassPathResource(path);
    }

    /**
     * 获取ClassPath下的资源路径
     *
     * @param pathBaseClassLoader 相对与资源路径
     * @return url
     */
    public static URL getResource(String pathBaseClassLoader) {
        return getResource(pathBaseClassLoader, null);
    }

    /**
     * 获取资源相对路径的URL
     *
     * @param path  资源相对路径
     * @param clazz Class,获取相对于此Class所在的路径，如果为{@Code null},则相对于Classpath
     * @return
     */
    public static URL getResource(String path, Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return ClassLoaderUtil.getClassLoader().getResource(path);
        }
        return clazz.getResource(path);
    }

    public static void main(String[] args) {
        ClassLoader classLoader = ResourcesUtil.class.getClassLoader();
        System.out.println(new File(classLoader.getResource("test1.txt").getFile()).exists());
        System.out.println(new File("test.txt").exists());

        System.out.println(ResourcesUtil.class);
        System.out.println(classLoader.getResource("test.txt"));
        System.out.println(ResourcesUtil.getResource("test.txt", ResourcesUtil.class));
    }
}
