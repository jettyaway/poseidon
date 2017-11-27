package com.voxlearning.poseidon.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-27
 * @since 17-11-27
 */
public class SimpleWatcher implements Watcher {
    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {

    }

    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {

    }

    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {

    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {

    }
}
