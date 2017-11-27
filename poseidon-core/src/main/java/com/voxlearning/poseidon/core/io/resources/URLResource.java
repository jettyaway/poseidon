package com.voxlearning.poseidon.core.io.resources;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.io.IOUtil;
import com.voxlearning.poseidon.core.util.URLUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/25
 * @since 2017/11/25
 */
public class URLResource implements Resources {
    protected URL url;

    public URLResource(URL url) {
        this.url = url;
    }

    public URLResource(File file) {

    }

    @Override
    public URL getURL() {
        return this.url;
    }

    @Override
    public InputStream getInputStream() {
        if (this.url == null) {
            throw new IORuntimeException("resource [%s] not exist.", this.url);
        }
        return URLUtil.getStream(this.url);

    }

    @Override
    public BufferedReader getBufferedReader(Charset charset) {
        return URLUtil.getReader(url, charset);
    }

    @Override
    public String readString(Charset charset) {
        BufferedReader reader;
        reader = getBufferedReader(charset);
        return IOUtil.read(reader);
    }

    @Override
    public String readUtf8String() {
        return readString(Charset.forName("UTF-8"));
    }

    @Override
    public byte[] readBytes() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream();
            return IOUtil.readBytes(inputStream);
        } finally {
            IOUtil.close(inputStream);
        }
    }

    public File getFile() {
        return FileUtil.file(this.url);
    }

    @Override
    public String toString() {
        return "URLResource{" +
                "url=" + url +
                '}';
    }
}
