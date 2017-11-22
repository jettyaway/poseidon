package com.voxlearning.poseidon.core.io.watch;

import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.io.IOUtil;
import com.voxlearning.poseidon.core.io.exception.IoRuntimeException;
import com.voxlearning.poseidon.core.io.exception.WatchException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-22
 * @since 17-11-22
 */
public class WatcherMonitor extends Thread implements Closeable {

    /**
     * 丢失事件
     */
    public static final WatchEvent.Kind<?> OVERFLOW = StandardWatchEventKinds.OVERFLOW;
    /**
     * 创建事件
     **/
    public static final WatchEvent.Kind<?> CREATE = StandardWatchEventKinds.ENTRY_CREATE;
    /**
     * 修改事件
     **/
    public static final WatchEvent.Kind<?> MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
    /**
     * 删除事件
     **/
    public static final WatchEvent.Kind<?> DELETE = StandardWatchEventKinds.ENTRY_DELETE;
    /**
     * 所有事件
     **/
    public static final WatchEvent.Kind<?>[] ALL_EVENTS = {
            OVERFLOW,
            CREATE,
            MODIFY,
            DELETE
    };


    /**
     * 监听路径 目录
     */
    private Path path;
    /**
     * 递归目录的最大深度
     */
    private int maxDepth;
    /**
     * 监听路径 文件
     */
    private Path filePath;

    /**
     * 监听服务
     */
    private WatchService watchService;


    /**
     * 监听者
     */
    private Watcher watcher;


    /**
     * 监听事件集合
     */
    private WatchEvent.Kind<?>[] events;

    /**
     * 监听是否关闭
     */
    private boolean isClose;

    private Map<WatchKey, Path> watchKeyPathMap = new HashMap<>();

    public WatcherMonitor(Path path, int maxDepth, WatchEvent.Kind<?>... events) {
        this.path = path;
        this.maxDepth = maxDepth;
        this.events = events;
        this.init();
    }

    public static WatcherMonitor createMonitor(URL url, WatchEvent.Kind<?>... events) {
        return createMonitor(url, 0, events);
    }

    public static WatcherMonitor createMonitor(URL url, int maxDepth, WatchEvent.Kind<?>... events) {
        try {
            return createMonitor(url.toURI(), maxDepth, events);
        } catch (URISyntaxException e) {
            throw new WatchException(e);
        }
    }

    public static WatcherMonitor createMonitor(URI uri, WatchEvent.Kind<?>... events) {
        return createMonitor(uri, 0, events);
    }

    public static WatcherMonitor createMonitor(URI uri, int maxDepth, WatchEvent.Kind<?>... events) {
        return createMonitor(Paths.get(uri), maxDepth, events);
    }

    public static WatcherMonitor createMonitor(File file, WatchEvent.Kind<?>... events) {
        return createMonitor(file, 0, events);
    }

    public static WatcherMonitor createMonitor(File file, int maxDepth, WatchEvent.Kind<?>... events) {
        return createMonitor(file.toPath(), maxDepth, events);
    }

    public static WatcherMonitor createMonitor(String path, WatchEvent.Kind<?>... events) {
        return createMonitor(path, 0, events);
    }

    public static WatcherMonitor createMonitor(String path, int maxDepth, WatchEvent.Kind<?>... events) {
        return createMonitor(Paths.get(path), maxDepth, events);
    }

    public static WatcherMonitor createMonitor(Path path, WatchEvent.Kind<?>[] events) {
        return new WatcherMonitor(path, 0, events);
    }

    public static WatcherMonitor createMonitor(Path path, int maxDepth, WatchEvent.Kind<?>[] events) {
        return new WatcherMonitor(path, maxDepth, events);
    }

    public static WatcherMonitor createAll(URI uri, Watcher watcher) {
        return createAll(uri, 0, watcher);
    }

    public static WatcherMonitor createAll(URI uri, int maxDepth, Watcher watcher) {
        return createAll(Paths.get(uri), maxDepth, watcher);
    }

    public static WatcherMonitor createAll(URL url, Watcher watcher) {
        return createAll(url, 0, watcher);
    }

    public static WatcherMonitor createAll(URL url, int maxDepth, Watcher watcher) {
        try {
            return createAll(url.toURI(), maxDepth, watcher);
        } catch (URISyntaxException e) {
            throw new WatchException(e);
        }
    }

    public static WatcherMonitor createAll(String path, Watcher watcher) {
        return createAll(path, 0, watcher);
    }

    public static WatcherMonitor createAll(String path, int maxDepth, Watcher watcher) {
        return createAll(Paths.get(path), maxDepth, watcher);
    }

    public static WatcherMonitor createAll(Path path, Watcher watcher) {
        return createAll(path, 0, watcher);
    }

    public static WatcherMonitor createAll(Path path, int maxDepth, Watcher watcher) {
        WatcherMonitor monitor = createMonitor(path, ALL_EVENTS);
        monitor.setMaxDepth(maxDepth);
        monitor.setWatcher(watcher);
        return monitor;
    }


    private static final String C_DOT = ".";
    private static final String C_DIR = ".d";

    private void init() {
        if (!Files.exists(this.path, LinkOption.NOFOLLOW_LINKS)) {
            Path path = FileUtil.getLastPathElement(this.path).orElse(null);
            //如果不存在，并且路径中的最后一部分包含"." （除了.d），那么就认为是要监听没有创建的文件，而不是目录.
            if (!Objects.isNull(path)) {
                String pathStr = path.toString();
                if (pathStr.contains(C_DOT) && !pathStr.endsWith(C_DIR)) {
                    this.filePath = this.path;
                    this.path = this.filePath.getParent();
                }
            }
            try {
                Files.createDirectories(this.path);
            } catch (IOException e) {
                throw new IoRuntimeException(e);
            }
        } else if (Files.isRegularFile(this.path, LinkOption.NOFOLLOW_LINKS)) {
            this.filePath = this.path;
            this.path = this.filePath.getParent();
        }
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new WatchException(e);
        }
        this.isClose = false;
    }


    public WatcherMonitor(Path path, WatchEvent.Kind<?>... events) {
        this(path, 0, events);
    }

    public WatcherMonitor(String path, WatchEvent.Kind<?>... events) {
        this(Paths.get(path), 0, events);
    }

    public WatcherMonitor setWatcher(Watcher watcher) {
        this.watcher = watcher;
        return this;
    }

    public WatcherMonitor setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    @Override
    public void run() {
        watch(this.watcher);
    }

    private void watch(Watcher watcher) {
        if (isClose) {
            throw new WatchException("WatchMonitor is closed");
        }
        registerPath();
        while (true) {
            WatchKey wk;
            try {
                //block
                wk = watchService.take();
            } catch (InterruptedException e) {
                return;
            }
            Path path = watchKeyPathMap.get(wk);
            wk.pollEvents().stream().filter(event -> Objects.isNull(this.filePath) || this.filePath.endsWith(event.context().toString()))
                    .forEach(event -> {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            watcher.onCreate(event, path);
                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            watcher.onModify(event, path);
                        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            watcher.onModify(event, path);
                        } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                            watcher.onModify(event, path);
                        }
                    });
            wk.reset();
        }

    }

    private void registerPath() {
        registerPath(this.path, Objects.isNull(this.filePath) ? maxDepth : 0);
    }

    private void registerPath(Path path, int maxDepth) {
        try {
            WatchKey watchKey = path.register(watchService, this.events == null || this.events.length == 0 ? ALL_EVENTS : this.events);
            watchKeyPathMap.put(watchKey, path);
            if (maxDepth > 1) {
                Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        registerPath(dir, 0);
                        return super.postVisitDirectory(dir, exc);
                    }
                });
            }
        } catch (IOException e) {
            if (e instanceof AccessDeniedException) {
                return;
            }
            throw new WatchException(e);
        }
    }

    @Override
    public void close() throws IOException {
        isClose = true;
        IOUtil.close(watchService);
    }
}
