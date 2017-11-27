package com.voxlearning.poseidon.core.io.resources;


import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.ClassLoaderUtil;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.net.URL;
import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/26
 * @since 2017/11/26
 */
public class ClassPathResource extends URLResource {

    private String path;
    private ClassLoader classLoader;
    private Class<?> clazz;

    public ClassPathResource(String path) {
        this(path, null, null);
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this(path, null, clazz);
    }

    public ClassPathResource(String path, ClassLoader loader) {
        this(path, loader, null);
    }


    public ClassPathResource(String pathBaseClassLoader, ClassLoader loader, Class<?> clazz) {
        super((URL) null);
        this.path = normalizePath(path);
        this.classLoader = Objects.isNull(loader) ? ClassLoaderUtil.getClassLoader() : loader;
        this.clazz = clazz;
        initUrl();
    }

    private void initUrl() {
        if (Objects.nonNull(clazz)) {
            super.url = clazz.getResource(this.path);
        } else if (Objects.nonNull(classLoader)) {
            super.url = classLoader.getResource(this.path);
        } else {
            super.url = ClassLoader.getSystemClassLoader().getResource(this.path);
        }
        if (Objects.isNull(super.url)) {
            throw new IORuntimeException("resource of path [%s] not exist!", this.path);
        }
    }

    @Override
    public String toString() {
        return Objects.isNull(this.path) ? super.toString() : "classpath:" + this.path;
    }

    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    private String normalizePath(String path) {
        path = FileUtil.normalize(path);
        path = StrUtil.removePrefix(path, StrUtil.SLASH);
        Preconditions.checkArgument(!FileUtil.isAbsolutePath(path), "path must be a relative path!");
        return path;
    }
}
