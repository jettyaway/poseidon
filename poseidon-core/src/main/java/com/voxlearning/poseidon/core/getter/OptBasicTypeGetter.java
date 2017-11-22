package com.voxlearning.poseidon.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * 基本类型getter,提供默认值选项
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-21
 * @since 17-11-21
 */
public interface OptBasicTypeGetter<K> {

    Optional<Object> getObj(K key, Object defaultValue);

    Optional<String> getStr(K key, String defaultValue);

    Optional<Integer> getInt(K key, Integer defaultValue);

    Optional<Short> getShort(K key, Short defaultValue);

    Optional<Boolean> getBoolean(K key, Boolean defaultValue);

    Optional<Long> getLong(K key, Long defaultValue);

    Optional<Character> getChar(K key, Character defaultValue);

    Optional<Float> getFloat(K key, Float defaultValue);

    Optional<Double> getDouble(K key, Double defaultValue);

    Optional<Byte> getByte(K key, Byte defaultValue);

    Optional<BigDecimal> getBigDecimal(K key, BigDecimal defaultValue);

    Optional<BigInteger> getBigInteger(K key, BigInteger defaultValue);

    /**
     * @param clazz Enum 的class
     * @param key   KEY
     * @param <E>   枚举类型
     * @return 枚举类型的值
     */
    <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, K key, E defaultValue);
}
