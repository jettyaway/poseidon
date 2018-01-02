package com.voxlearning.poseidon.storage.hdfs.bucket;

import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.storage.hdfs.bean.HdfsEvent;
import com.voxlearning.poseidon.storage.hdfs.exception.HdfsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.List;
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
     * 空闲时间
     */
    private final long idleTimeout;

    /**
     * rename 重试间隔
     */
    private final long retryInterval;

    private volatile ScheduledFuture<Void> idleFuture;

    private volatile ScheduledFuture<Void> timedRollFuture;


    private static final Integer staticLock = new Integer(1);

    private final AtomicLong fileExtensionCounter;

    private static final String defaultInUseSuffix = ".tmp";

    private HdfsWriter hdfsWriter;

    public BucketWriter(long rollInterval, long rollSize, long batchSize, String filePath, String fileName,
                        ScheduledExecutorService timeRollerPool, ExecutorService callTimeOutPool, int maxRenameTries,
                        long callTimeout, long retryInterval, long idleTimeout) {
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
        this.idleTimeout = idleTimeout;
        fileExtensionCounter = new AtomicLong(System.currentTimeMillis());
        hdfsWriter = new HdfsDataStream();
        try {
            open();
        } catch (IOException e) {
            LOGGER.warn("{}", e);
        }
    }

    private void open() throws IOException {
        if (Objects.isNull(filePath) || Objects.isNull(hdfsWriter)) {
            throw new IOException("Invalid file settings");
        }
        synchronized (staticLock) {
            long counter = fileExtensionCounter.getAndIncrement();
            String fullFileName = fileName + StrUtil.DOT + counter;
            bucketPath = filePath + StrUtil.SLASH + fullFileName + defaultInUseSuffix;
            targetPath = filePath + StrUtil.SLASH + fullFileName;
            //open bucket path
            try {
                hdfsWriter.open(bucketPath);
            } catch (IOException e) {
                throw new HdfsRuntimeException("open bucketPath[%s] failed.", bucketPath);
            }
        }
        //按时间滚动文件
        if (rollInterval > 0) {
            Callable<Void> action = () -> {
                LOGGER.info("Rolling file({}),Roll scheduled after {} sec elapsed.", bucketPath, rollInterval);
                try {
                    close(true);
                } catch (Throwable t) {
                    LOGGER.error("UnExpected error", t);
                }
                return null;
            };
            timedRollFuture = timeRollerPool.schedule(action, rollInterval, TimeUnit.SECONDS);
        }
        isOpen = true;
    }

    public synchronized void append(final HdfsEvent hdfsEvent) throws IOException, InterruptedException {
        if (Objects.isNull(hdfsEvent)) {
            return;
        }
        //每次append时需要取消idleFuture
        if (Objects.nonNull(idleFuture)) {
            idleFuture.cancel(false);
            //每次取消时 不能中断线程，否则hdfs要抛异常，如果idleFuture 已经跑起来了，那就不能取消
            //需要等待该线程完成再继续写
            if (!idleFuture.isDone()) {
                try {
                    idleFuture.get(callTimeout, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    LOGGER.warn("Timeout while trying to cancel closing of idle file.Idle file close may hava failed.", e);
                } catch (Exception e) {
                    LOGGER.warn("Error while trying to cancel closing of idle file.", e);
                }
            }
            idleFuture = null;
        }

        if (!isOpen) {
            open();
        }
        if (shouldRotate()) {
            close();
            open();
        }
        try {
            //add attempt counter
            callWithTimeout(() -> {
                hdfsWriter.append(hdfsEvent.getBody());
                return null;
            });
        } catch (IOException e) {

        }
    }

    public synchronized void append(final List<HdfsEvent> hdfsEventList) {

    }

    /**
     * 关闭文件，并且不调用回调函数
     */
    public synchronized void close() throws IOException, InterruptedException {
        close(false);
    }

    /**
     * 关闭文件
     *
     * @param callCloseCallback 是否调用回调函数
     */
    public synchronized void close(boolean callCloseCallback) throws IOException, InterruptedException {

    }


    /**
     * flush the data
     * 第一次调用flush时启动timeRoller 线程,延迟IdleTimeout 时间
     * 调用{@see close} 方法,之后每次调用flush 都将之前的roller线程取消并且重置,
     * 这样来判断在idleTimeout时间内是否有进行写操作
     *
     * @throws IOException          io 错误时抛出
     * @throws InterruptedException 被中断时抛出
     */
    public synchronized void flush() throws IOException, InterruptedException {
        //判断batch是否完成，完成则不需要flush
        if (isBatchComplete()) {
            return;
        }
        //调用writer flush
        callWithTimeout(() -> {
            hdfsWriter.sync();
            return null;
        });
        //重置batchCounter
        batchCounter = 0;

        if (idleTimeout <= 0) {
            return;
        }
        //如果future 存在，并且不能被取消，说明已经运行起来或者已经被取消了
        if (Objects.isNull(idleFuture) || idleFuture.cancel(false)) {
            Callable<Void> idleAction = () -> {
                LOGGER.info("Closing idle bucketWriter {} at {}", bucketPath, System.currentTimeMillis());
                if (isOpen) {
                    close(false);
                }
                return null;
            };

            idleFuture = timeRollerPool.schedule(idleAction, idleTimeout, TimeUnit.SECONDS);
        }
    }

    private boolean isBatchComplete() {
        return batchCounter == 0;
    }

    /**
     * check if time to rotate the file
     * 当前只支持按文件大小滚动
     *
     * @return true or false
     */
    private boolean shouldRotate() {
        boolean doRotate = false;
        if ((rollSize > 0) && (rollSize <= processSize)) {
            LOGGER.info("rolling:rollSize:{},bytes:{}", rollSize, processSize);
            doRotate = true;
        }
        return doRotate;
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

    /**
     * This method if the current thread has been interrupted and throws an
     * exception.
     *
     * @throws InterruptedException
     */
    private static void checkAndThrowInterruptedException()
            throws InterruptedException {
        if (Thread.currentThread().interrupted()) {
            throw new InterruptedException("Timed out before HDFS call was made. "
                    + "Your hdfs.callTimeout might be set too low or HDFS calls are "
                    + "taking too long.");
        }
    }


}
