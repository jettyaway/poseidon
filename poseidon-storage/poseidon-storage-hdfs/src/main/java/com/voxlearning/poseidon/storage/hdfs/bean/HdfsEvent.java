package com.voxlearning.poseidon.storage.hdfs.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * hdfs data object
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-29
 * @since 17-12-29
 */
public class HdfsEvent {

    private byte[] body;

    private Map<String, String> headers = new HashMap<>();

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "HdfsEvent{" +
                "body=" + Arrays.toString(body) +
                ", headers=" + headers +
                '}';
    }
}
