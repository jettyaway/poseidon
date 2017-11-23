package com.voxlearning.poseidon.core.lang;


import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 简单缓存类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class SimpleCache<K, V> {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Map<K, V> cache = new WeakHashMap<K, V>();
    private final ReadLock readLock = readWriteLock.readLock();
    private final WriteLock writeLock = readWriteLock.writeLock();

    public V get(K key) {
        readLock.lock();
        V value;
        try {
            value = cache.get(key);
        } finally {
            readLock.unlock();
        }
        return value;
    }

    public V put(K key, V value) {
        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
        return value;
    }

    public V remove(K key) {
        writeLock.lock();

        try {
            return cache.remove(key);
        } finally {
            writeLock.unlock();
        }
    }


    public void clear() {
        writeLock.lock();
        try {
            cache.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
