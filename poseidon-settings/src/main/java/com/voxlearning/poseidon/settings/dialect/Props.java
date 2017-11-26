package com.voxlearning.poseidon.settings.dialect;

import com.voxlearning.poseidon.core.getter.BasicTypeGetter;
import com.voxlearning.poseidon.core.getter.OptBasicTypeGetter;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import com.voxlearning.poseidon.core.util.CharsetUtil;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Properties;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-21
 * @since 17-11-21
 */
public final class Props extends Properties implements BasicTypeGetter, OptBasicTypeGetter {


    private static final long serialVersionUID = -1202889668984386112L;

    private URL propertiesFileUrl;

    private WatcherMonitor watcherMonitor;
    private Charset charset = CharsetUtil.CHARSET_ISO_8859_1;

    private Props(String path, Charset charset) {

    }


    @Override
    public Optional<Object> getObj(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public Optional<String> getStr(Object key, String defaultValue) {
        return null;
    }

    @Override
    public Optional<Integer> getInt(Object key, Integer defaultValue) {
        return null;
    }

    @Override
    public Optional<Short> getShort(Object key, Short defaultValue) {
        return null;
    }

    @Override
    public Optional<Boolean> getBoolean(Object key, Boolean defaultValue) {
        return null;
    }

    @Override
    public Optional<Long> getLong(Object key, Long defaultValue) {
        return null;
    }

    @Override
    public Optional<Character> getChar(Object key, Character defaultValue) {
        return null;
    }

    @Override
    public Optional<Float> getFloat(Object key, Float defaultValue) {
        return null;
    }

    @Override
    public Optional<Double> getDouble(Object key, Double defaultValue) {
        return null;
    }

    @Override
    public Optional<Byte> getByte(Object key, Byte defaultValue) {
        return null;
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(Object key, BigDecimal defaultValue) {
        return null;
    }

    @Override
    public Optional<BigInteger> getBigInteger(Object key, BigInteger defaultValue) {
        return null;
    }

    @Override
    public Optional getEnum(Class clazz, Object key, Enum defaultValue) {
        return null;
    }

    @Override
    public Optional<Object> getObj(Object key) {
        return null;
    }

    @Override
    public Optional<String> getStr(Object key) {
        return null;
    }

    @Override
    public Optional<Integer> getInt(Object key) {
        return null;
    }

    @Override
    public Optional<Short> getShort(Object key) {
        return null;
    }

    @Override
    public Optional<Boolean> getBoolean(Object key) {
        return null;
    }

    @Override
    public Optional<Long> getLong(Object key) {
        return null;
    }

    @Override
    public Optional<Character> getChar(Object key) {
        return null;
    }

    @Override
    public Optional<Float> getFloat(Object key) {
        return null;
    }

    @Override
    public Optional<Double> getDouble(Object key) {
        return null;
    }

    @Override
    public Optional<Byte> getByte(Object key) {
        return null;
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(Object key) {
        return null;
    }

    @Override
    public Optional<BigInteger> getBigInteger(Object key) {
        return null;
    }

    @Override
    public Optional getEnum(Class clazz, Object key) {
        return null;
    }
}
