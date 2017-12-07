package com.voxlearning.poseidon.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-07
 * @since 17-12-7
 */
public abstract class OptNullBasicTypeGetter<K> implements BasicTypeGetter<K>, OptBasicTypeGetter<K> {

    @Override
    public Optional<Object> getObj(K key) {
        return getObj(key, null);
    }

    @Override
    public Optional<String> getStr(K key) {
        return getStr(key, null);
    }

    @Override
    public Optional<Integer> getInt(K key) {
        return getInt(key, null);
    }

    @Override
    public Optional<Long> getLong(K key) {
        return getLong(key, null);
    }

    @Override
    public Optional<Float> getFloat(K key) {
        return getFloat(key, null);
    }

    @Override
    public Optional<Short> getShort(K key) {
        return getShort(key, null);
    }

    @Override
    public Optional<Boolean> getBoolean(K key) {
        return getBoolean(key, null);
    }

    @Override
    public Optional<Byte> getByte(K key) {
        return getByte(key, null);
    }

    @Override
    public Optional<Double> getDouble(K key) {
        return getDouble(key, null);
    }

    @Override
    public Optional<BigInteger> getBigInteger(K key) {
        return getBigInteger(key, null);
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(K key) {
        return getBigDecimal(key, null);
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, K key) {
        return getEnum(clazz, key);
    }

    @Override
    public Optional<Character> getChar(K key) {
        return getChar(key, null);
    }
}
