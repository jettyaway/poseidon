package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.core.io.watch.Watcher;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 监听文件测试类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class WatchMonitorTest {
    private Logger logger = LoggerFactory.getLogger(WatchMonitorTest.class);

    @Test
    public void testMonitor() {
        WatcherMonitor.createAll("/home/suhao/gitproject/poseidon/poseidon-settings/target/test-classes/test.properties", new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {

            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                logger.debug("currentPath:{}", currentPath);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {

            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {

            }
        }).start();

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
