package com.voxlearning.poseidon.core.io.resources;


import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.util.URLUtil;

import java.io.File;


/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/26
 * @since 2017/11/26
 */
public class FileResource extends URLResource {

    public FileResource(File file) {
        super(URLUtil.getURL(file));
    }

    public FileResource(String path) {
        this(FileUtil.file(path));
    }
}
