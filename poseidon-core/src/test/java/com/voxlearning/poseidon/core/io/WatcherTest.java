package com.voxlearning.poseidon.core.io;

import com.voxlearning.poseidon.core.io.watch.Watcher;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class WatcherTest {

    @Test
    public void testMonitor() {
        WatcherMonitor monitor = WatcherMonitor.createMonitor("/home/suhao/Desktop/test.txt", WatcherMonitor.ALL_EVENTS);
        monitor.setWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                System.out.println(event.kind() + "," + event.context().toString() + "," + currentPath);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                System.out.println(event.kind() + "," + event.context().toString() + "," + currentPath);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                System.out.println(event.kind() + "," + event.context().toString() + "," + currentPath);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                System.out.println(event.kind() + "," + event.context().toString() + "," + currentPath);
            }
        });
        monitor.start();
        try {
            monitor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
