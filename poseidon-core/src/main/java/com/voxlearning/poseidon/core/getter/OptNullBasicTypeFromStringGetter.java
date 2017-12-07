package com.voxlearning.poseidon.core.getter;

import com.voxlearning.poseidon.core.convert.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

/**
 * 基本類型的getter實現抽象類,所有类型的值都通过getStr获取转化而来</br>
 * 用户只需要提供getStr方法即可<br/>
 * 如果值不存在 则返回<code>null</code>
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-07
 * @since 17-12-7
 */
public abstract class OptNullBasicTypeFromStringGetter<K> extends OptNullBasicTypeGetter<K> {
    @Override
    public abstract Optional<String> getStr(K key, String defaultValue);


    @Override
    public Optional<Object> getObj(K key, Object defaultValue) {
        return Optional.ofNullable(getStr(key, Objects.isNull(defaultValue) ? null : defaultValue.toString()));
    }

    @Override
    public Optional<Integer> getInt(K key, Integer defaultValue) {
        return Optional.ofNullable(Convert.toInt(getStr(key), defaultValue));
    }

    @Override
    public Optional<Float> getFloat(K key, Float defaultValue) {
        return Optional.ofNullable(Convert.toFloat(getStr(key), defaultValue));
    }

    @Override
    public Optional<Long> getLong(K key, Long defaultValue) {
        return Optional.ofNullable(Convert.toLong(getStr(key), defaultValue));
    }

    @Override
    public Optional<Double> getDouble(K key, Double defaultValue) {
        return Optional.ofNullable(Convert.toDouble(getStr(key), defaultValue));
    }

    @Override
    public Optional<Byte> getByte(K key, Byte defaultValue) {
        return Optional.ofNullable(Convert.toByte(getStr(key), defaultValue));
    }

    @Override
    public Optional<Short> getShort(K key, Short defaultValue) {
        return Optional.ofNullable(Convert.toShort(getStr(key), defaultValue));
    }

    @Override
    public Optional<Boolean> getBoolean(K key, Boolean defaultValue) {
        return Optional.ofNullable(Convert.toBoolean(getStr(key), defaultValue));
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, K key, E defaultValue) {
        return Optional.ofNullable(Convert.toEnum(clazz, key, defaultValue));
    }

    @Override
    public Optional<Character> getChar(K key, Character defaultValue) {
        return Optional.ofNullable(Convert.toChar(getStr(key), defaultValue));
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(K key, BigDecimal defaultValue) {
        return Optional.ofNullable(Convert.toBigDecimal(getStr(key), defaultValue));
    }

    @Override
    public Optional<BigInteger> getBigInteger(K key, BigInteger defaultValue) {
        return Optional.ofNullable(Convert.toBigInteger(getStr(key), defaultValue));
    }
}
