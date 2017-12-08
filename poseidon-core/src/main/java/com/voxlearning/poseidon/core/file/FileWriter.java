package com.voxlearning.poseidon.core.file;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.io.exception.IoRuntimeException;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.CharsetUtil;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class FileWriter extends FileWrapper {

    public static FileWriter create(File file) {
        return new FileWriter(file);
    }

    public static FileWriter create(String filePath) {
        return new FileWriter(filePath);
    }

    public static FileWriter create(File file, Charset charset) {
        return new FileWriter(file, charset);
    }

    public static FileWriter create(String filePath, Charset charset) {
        return new FileWriter(filePath, charset);
    }

    public FileWriter(File file, Charset charset) {
        super(file, charset);
        checkFile(file);
    }

    public FileWriter(File file, String charset) {
        this(file, CharsetUtil.charset(charset));
    }

    public FileWriter(String filePath, Charset charsetName) {
        this(FileUtil.file(filePath), charsetName);
    }

    public FileWriter(String filePath, String charsetName) {
        this(FileUtil.file(filePath), CharsetUtil.charset(charsetName));
    }

    public FileWriter(File file) {
        this(file, DEFAULT_CHARSET);
    }

    public FileWriter(String filePath) {
        this(FileUtil.file(filePath), DEFAULT_CHARSET);
    }


    //------------------------------------------------------------------------------------------------------------

    public BufferedWriter getWriter(boolean append) {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileUtil.touch(file), append), charset));
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    private void checkFile(File file) {
        Preconditions.checkNotNull(file);
        if (this.file.exists() && !this.file.isFile()) {
            throw new IoRuntimeException("File[%s] is not a regural file");
        }
    }
}
