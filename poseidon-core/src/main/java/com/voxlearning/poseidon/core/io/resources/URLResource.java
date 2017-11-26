package com.voxlearning.poseidon.core.io.resources;

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
        return null;
    }

    @Override
    public BufferedReader getBufferedReader(Charset charset) {
        return null;
    }

    @Override
    public String readString(Charset charset) {
        return null;
    }

    @Override
    public String readUtf8String(Charset charset) {
        return null;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }
}
