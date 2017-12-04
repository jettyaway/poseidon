package com.voxlearning.poseidon.settings.dialect;

import com.voxlearning.poseidon.core.convert.Convert;
import com.voxlearning.poseidon.core.getter.BasicTypeGetter;
import com.voxlearning.poseidon.core.getter.OptBasicTypeGetter;
import com.voxlearning.poseidon.core.io.resources.*;
import com.voxlearning.poseidon.core.io.watch.SimpleWatcher;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.CharsetUtil;
import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.settings.expception.SettingRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-21
 * @since 17-11-21
 */
public final class Props extends Properties implements BasicTypeGetter<String>, OptBasicTypeGetter<String> {

    private static final long serialVersionUID = -1202889668984386112L;

    private static final Logger logger = LoggerFactory.getLogger(Props.class);

    private URL propertiesFileUrl;

    private WatcherMonitor watcherMonitor;
    private Charset charset = CharsetUtil.CHARSET_ISO_8859_1;

    public static Properties getProperties(String path) {
        return new Props(path);
    }

    public static Props getProperties(String path, String charsetName) {
        return new Props(path, charsetName);
    }

    public static Props getProperties(String path, Charset charset) {
        return new Props(path, charset);
    }

    public static Props getPropertiesAutoReload(String path) {
        return new Props(path).autoLoad();
    }

    public static Props getPropertiesAutoReload(String path, String charsetName) {
        return new Props(path, charsetName).autoLoad();
    }

    public static Props getPropertiesAutoReload(String path, Charset charset) {
        return new Props(path, charset).autoLoad();
    }


    public Props() {
        super();
    }


    public Props(File file) {
        this(file, CharsetUtil.CHARSET_ISO_8859_1);
    }

    public Props(File file, String charsetName) {
        this(file, CharsetUtil.charset(charsetName));
    }

    public Props(File file, Charset charset) {
        Preconditions.checkNotNull(file);
        this.charset = charset;
        this.load(new FileResource(file));
    }

    public Props(String path) {
        this(path, CharsetUtil.CHARSET_ISO_8859_1);
    }

    public Props(String path, String charsetName) {
        this(path, CharsetUtil.charset(charsetName));
    }


    public Props(String path, Charset charset) {
        Preconditions.checkArgument(StrUtil.isNotBlank(path));
        if (Objects.isNull(charset)) {
            this.charset = charset;
        }
        this.load(ResourcesUtil.getResourceObj(path));
    }


    public Props(String path, Class<?> clazz) {
        this(path, clazz, CharsetUtil.CHARSET_ISO_8859_1);
    }

    public Props(String path, Class<?> clazz, String charsetName) {
        this(path, clazz, CharsetUtil.charset(charsetName));
    }

    public Props(String path, Class<?> clazz, Charset charset) {
        Preconditions.checkNotNull(path);
        if (!Objects.isNull(charset)) {
            this.charset = charset;
        }
        this.load(new ClassPathResource(path, clazz));
    }

    public Props(URL url) {
        this(url, CharsetUtil.CHARSET_ISO_8859_1);
    }

    public Props(URL url, String charsetName) {
        this(url, CharsetUtil.charset(charsetName));
    }

    public Props(URL url, Charset charset) {
        Preconditions.checkNotNull(url);
        if (Objects.nonNull(charset)) {
            this.charset = charset;
        }
        this.load(new URLResource(url));
    }

    public Props(Properties properties) {
        if (properties != null && !properties.isEmpty()) {
            this.putAll(properties);
        }
    }


    public void load(Resources urlResource) {
        this.propertiesFileUrl = urlResource.getURL();
        if (Objects.isNull(propertiesFileUrl)) {
            throw new SettingRuntimeException("can not find properties file:[%s]", urlResource);
        }
        logger.debug("Load properties [{}]", propertiesFileUrl.getPath());
        try {
            super.load(urlResource.getBufferedReader(charset));
        } catch (IOException e) {
            logger.error("Load properties [{}] error", propertiesFileUrl.getPath(), e);
        }
    }

    /**
     * 重新加载
     */
    public void load() {
        this.load(new URLResource(this.propertiesFileUrl));
    }

    /**
     * 自动加载
     */
    public Props autoLoad() {
        if (Objects.nonNull(this.watcherMonitor)) {
            this.watcherMonitor.close();
        }
        this.watcherMonitor = WatcherMonitor.createMonitor(this.propertiesFileUrl);
        watcherMonitor.setWatcher(new SimpleWatcher() {
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                //TODO check md5 value
                load();
            }
        });
        return this;
    }


    @Override
    public Optional<Object> getObj(String key, Object defaultValue) {
        Object value = getStr(key, Objects.isNull(defaultValue) ? null : defaultValue.toString()).orElse(null);
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<String> getStr(String key, String defaultValue) {
        return Optional.ofNullable(super.getProperty(key, defaultValue));
    }

    @Override
    public Optional<Integer> getInt(String key, Integer defaultValue) {
        return Optional.ofNullable(Convert.toInt(getStr(key), defaultValue));
    }

    @Override
    public Optional<Short> getShort(String key, Short defaultValue) {
        return Optional.ofNullable(Convert.toShort(getStr(key), defaultValue));
    }

    @Override
    public Optional<Boolean> getBoolean(String key, Boolean defaultValue) {
        return Optional.ofNullable(Convert.toBoolean(getStr(key), defaultValue));
    }

    @Override
    public Optional<Long> getLong(String key, Long defaultValue) {
        return Optional.ofNullable(Convert.toLong(getStr(key), defaultValue));
    }

    @Override
    public Optional<Character> getChar(String key, Character defaultValue) {
        return Optional.ofNullable(Convert.toChar(getStr(key), defaultValue));
    }

    @Override
    public Optional<Float> getFloat(String key, Float defaultValue) {
        return Optional.ofNullable(Convert.toFloat(getStr(key), defaultValue));
    }

    @Override
    public Optional<Double> getDouble(String key, Double defaultValue) {
        return Optional.ofNullable(Convert.toDouble(getStr(key), defaultValue));
    }

    @Override
    public Optional<Byte> getByte(String key, Byte defaultValue) {
        return Optional.ofNullable(Convert.toByte(getStr(key), defaultValue));
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(String key, BigDecimal defaultValue) {
        return Optional.ofNullable(Convert.toBigDecimal(getStr(key), defaultValue));
    }

    @Override
    public Optional<BigInteger> getBigInteger(String key, BigInteger defaultValue) {
        return Optional.ofNullable(Convert.toBigInteger(getStr(key), defaultValue));
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, String key, E defaultValue) {
        return Optional.ofNullable(Convert.toEnum(clazz, getStr(key), defaultValue));
    }

    @Override
    public Optional<Object> getObj(String key) {
        return getObj(key, null);
    }

    @Override
    public Optional<String> getStr(String key) {
        return Optional.ofNullable(super.getProperty(key));
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return getInt(key, null);
    }

    @Override
    public Optional<Short> getShort(String key) {
        return getShort(key, null);
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        return getBoolean(key, null);
    }

    @Override
    public Optional<Long> getLong(String key) {
        return getLong(key, null);
    }

    @Override
    public Optional<Character> getChar(String key) {
        return getChar(key, null);
    }

    @Override
    public Optional<Float> getFloat(String key) {
        return getFloat(key, null);
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return getDouble(key, null);
    }

    @Override
    public Optional<Byte> getByte(String key) {
        return getByte(key, null);
    }

    @Override
    public Optional<BigDecimal> getBigDecimal(String key) {
        return getBigDecimal(key, null);
    }

    @Override
    public Optional<BigInteger> getBigInteger(String key) {
        return getBigInteger(key, null);
    }

    @Override
    public <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, String key) {
        return getEnum(clazz,key,null);
    }
}
