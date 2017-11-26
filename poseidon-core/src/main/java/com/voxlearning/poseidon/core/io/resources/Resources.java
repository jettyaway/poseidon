package com.voxlearning.poseidon.core.io.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by hao.su
 * Created by 2017/11/25
 * History 2017/11/25.
 */
public interface Resources {

    URL getURL();

    InputStream getInputStream();

    BufferedReader getBufferedReader(Charset charset);

    String readString(Charset charset);

    String readUtf8String(Charset charset);

    byte[] readBytes() throws IOException;

}

