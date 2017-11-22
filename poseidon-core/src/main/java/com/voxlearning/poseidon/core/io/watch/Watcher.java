package com.voxlearning.poseidon.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 监听器【观察者】
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public interface Watcher {
    /**
     * 当发生create事件调用此方法
     *
     * @param event       监听事件
     * @param currentPath 当前路径
     */
    void onCreate(WatchEvent<?> event, Path currentPath);

    /**
     * 当发生modify事件调用此方法
     *
     * @param event       监听事件
     * @param currentPath 当前路径
     */
    void onModify(WatchEvent<?> event, Path currentPath);

    /**
     * 当发生delete事件调用此方法
     *
     * @param event       监听事件
     * @param currentPath 当前路径
     */
    void onDelete(WatchEvent<?> event, Path currentPath);

    /**
     * 当发生overflow事件调用此方法
     *
     * @param event       监听事件
     * @param currentPath 当前路径
     */
    void onOverflow(WatchEvent<?> event, Path currentPath);
}
