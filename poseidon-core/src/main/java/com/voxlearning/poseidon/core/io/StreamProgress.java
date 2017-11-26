package com.voxlearning.poseidon.core.io;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017/11/26
 * @since 2017/11/26
 */
public interface StreamProgress {

    void start();

    void progress(long progressSize);

    void finish();
}
