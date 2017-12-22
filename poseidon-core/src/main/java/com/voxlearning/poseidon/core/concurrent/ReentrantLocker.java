package com.voxlearning.poseidon.core.concurrent;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLocker {

    protected final ReentrantLock reentrantLock;

    public ReentrantLocker() {
        reentrantLock = newReentrantLock();
    }

    protected ReentrantLock newReentrantLock() {
        return new ReentrantLock();
    }

    public <T> T doInLock(LockCallback<T> callback) {
        Objects.requireNonNull(callback);
        reentrantLock.lock();
        try {
            return callback.callback();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void doInLockWithoutResult(LockCallbackWithoutResult callback) {
        Objects.requireNonNull(callback);
        reentrantLock.lock();
        try {
            callback.callback();
        } finally {
            reentrantLock.unlock();
        }
    }

}
