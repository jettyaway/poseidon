package com.voxlearning.poseidon.storage.hdfs.bucket;

import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.storage.hdfs.bean.HdfsEvent;
import com.voxlearning.poseidon.storage.hdfs.exception.HdfsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-29
 * @since 17-12-29
 */
public class BucketWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketWriter.class);

    /**
     * 文件回滚时间
     */
    private final long rollInterval;

    /**
     * 文件回滚大小
     */
    private final long rollSize;

    /**
     * 设置的文件batch大小
     */
    private final long batchSize;

    /**
     * 处理的数据大小,决定回滚文件大小
     */
    private long processSize;

    /**
     * 处理的event
     */
    private long eventCounter;


    /**
     * hdfs file System
     */
    private FileSystem fileSystem;

    /**
     * 文件路径
     */
    private volatile String filePath;

    /**
     * 文件名(实际是文件前缀)
     */
    private volatile String fileName;


    /**
     * 正在写的path
     */
    private volatile String bucketPath;

    /**
     * rename后的path
     */
    private volatile String targetPath;

    /**
     * batch大小
     */
    private volatile long batchCounter;

    /**
     * 当前文件是否打开
     */
    private volatile boolean isOpen;


    /**
     * callTimeOut 执行线程池
     */
    private final ExecutorService callTimeOutPool;

    /**
     * 时间滚动线程池
     */
    private final ScheduledExecutorService timeRollerPool;

    /**
     * rename 重试次数
     */
    private final int maxRenameTries;

    /**
     * 超时时间
     */
    private final long callTimeout;

    /**
     * rename 重试间隔
     */
    private final long retryInterval;


    private static final Integer staticLock = new Integer(1);

    private final AtomicLong fileExtensionCounter;

    private static final String defaultInUseSuffix = ".tmp";

    private HdfsWriter hdfsWriter;

    public BucketWriter(long rollInterval, long rollSize, long batchSize, String filePath, String fileName,
                        ScheduledExecutorService timeRollerPool, ExecutorService callTimeOutPool, int maxRenameTries,
                        long callTimeout, long retryInterval) {
        this.rollInterval = rollInterval;
        this.rollSize = rollSize;
        this.batchSize = batchSize;
        this.filePath = filePath;
        this.fileName = fileName;
        this.timeRollerPool = timeRollerPool;
        this.callTimeOutPool = callTimeOutPool;
        this.maxRenameTries = maxRenameTries;
        this.callTimeout = callTimeout;
        this.retryInterval = retryInterval;
        fileExtensionCounter = new AtomicLong(System.currentTimeMillis());
        hdfsWriter = new HdfsDataStream();
        open();
    }

    private void open() {
        long counter = fileExtensionCounter.getAndIncrement();
        String fullFileName = fileName + StrUtil.DOT + counter;
        bucketPath = filePath + StrUtil.SLASH + fullFileName + defaultInUseSuffix;
        targetPath = filePath + StrUtil.SLASH + fullFileName;

        synchronized (staticLock) {
            //open bucket path
            try {
                hdfsWriter.open(bucketPath);
            } catch (IOException e) {
                throw new HdfsRuntimeException("open bucketPath[%s] failed.", bucketPath);
            }
        }
    }

    public synchronized void append(final HdfsEvent hdfsEvent) {
        if (Objects.isNull(hdfsEvent)) {
            return;
        }
    }


    public synchronized void close() {

    }


    public synchronized void flush() {
        if (isBatchComplete()) {
            return;
        }
    }

    private boolean isBatchComplete() {
        return batchCounter == 0;
    }


    private <T> T callWithTimeout(final CallRunner<T> callRunner)
            throws IOException, InterruptedException {
        Future<T> future = callTimeOutPool.submit(() -> callRunner.call());
        try {
            if (callTimeout > 0) {
                return future.get(callTimeout, TimeUnit.MILLISECONDS);
            } else {
                return future.get();
            }
        } catch (TimeoutException eT) {
            //TODO 计数
            future.cancel(true);
            throw new IOException("Callable timed out after " +
                    callTimeout + " ms" + " on file: " + bucketPath, eT);
        } catch (ExecutionException e1) {
            //TODO 计数
            Throwable cause = e1.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else if (cause instanceof InterruptedException) {
                throw (InterruptedException) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new RuntimeException(e1);
            }
        } catch (CancellationException ce) {
            throw new InterruptedException(
                    "Blocked callable interrupted by rotation event");
        } catch (InterruptedException ex) {
            LOGGER.warn("Unexpected Exception " + ex.getMessage(), ex);
            throw ex;
        }
    }


    private interface CallRunner<T> {
        T call() throws Exception;
    }


}
