package com.voxlearning.poseidon.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * 基本类型Getter接口
 * 提供统一的getter访问接口
 *
 * @author <a href="mailto:hao.su@17zuoyue.com">hao.su</a>
 * @version 2017-11-21
 * @since 17-11-21
 */
public interface BasicTypeGetter<K> {

    Optional<Object> getObj(K key);

    Optional<String> getStr(K key);

    Optional<Integer> getInt(K key);

    Optional<Short> getShort(K key);

    Optional<Boolean> getBoolean(K key);

    Optional<Long> getLong(K key);

    Optional<Character> getChar(K key);

    Optional<Float> getFloat(K key);

    Optional<Double> getDouble(K key);

    Optional<Byte> getByte(K key);

    Optional<BigDecimal> getBigDecimal(K key);

    Optional<BigInteger> getBigInteger(K key);

    /**
     * @param clazz Enum 的class
     * @param key   KEY
     * @param <E>   枚举类型
     * @return 枚举类型的值
     */
    <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, K key);
}
