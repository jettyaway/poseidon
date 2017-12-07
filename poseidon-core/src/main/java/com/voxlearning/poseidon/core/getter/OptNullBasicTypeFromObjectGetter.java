package com.voxlearning.poseidon.core.getter;

import com.voxlearning.poseidon.core.convert.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * 基本類型的getter實現抽象類,所有类型的值都通过getObject获取转化而来</br>
 * 用户只需要提供getObject方法即可<br/>
 * 如果值不存在 则返回<code>null</code>
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-07
 * @since 17-12-7
 */
public abstract class OptNullBasicTypeFromObjectGetter<K> extends OptNullBasicTypeGetter<K> {

    @Override
    public abstract Optional<Object> getObj(K key, Object defaultValue);

    @Override
    public Optional<String> getStr(K key, String defaultValue) {
        return Optional.ofNullable(Convert.toStr(getObj(key), defaultValue));
    }

    @Override
    public Optional<Short> getShort(K key, Short defaultValue) {
        return Optional.ofNullable(Convert.toShort(getObj(key), defaultValue));
    }

    @Override
    public Optional<Byte> getByte(K key, Byte defaultValue) {
        return Optional.ofNullable(Convert.toByte(getObj(key), defaultValue));
    }

    @Override
    public Optional<Boolean> getBoolean(K key, Boolean defaultValue) {
        return Optional.ofNullable(Convert.toBoolean(getObj(key), defaultValue));
    }

    @Override
    public Optional<Integer> getInt(K key, Integer defaultValue) {
        return Optional.ofNullable(Convert.toInt(getObj(key), defaultValue));
    }

    @Override
    public Optional<Float> getFloat(K key, Float defaultValue) {
        return Optional.ofNullable(Convert.toFloat(getObj(key), defaultValue));
    }

    @Override
    public Optional<Long> getLong(K key, Long defaultValue) {
        return Optional.ofNullable(Convert.toLong(getObj(key), defaultValue));
    }

    @Override
    public Optional<Double> getDouble(K key, Double defaultValue) {
        return Optional.ofNullable(Convert.toDouble(getObj(key), defaultValue));
    }

    @Override
    public Optional<BigInteger> getBigInteger(K key, BigInteger defaultValue) {
        return Optional.ofNullable(Convert.toBigInteger(getObj(key), defaultValue));
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(K key, BigDecimal defaultValue) {
        return Optional.ofNullable(Convert.toBigDecimal(getObj(key), defaultValue));
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, K key, E defaultValue) {
        return Optional.ofNullable(Convert.toEnum(clazz, getObj(key), defaultValue));
    }
}
