package com.voxlearning.poseidon.settings.dialect;

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
public final class Props extends Properties implements BasicTypeGetter, OptBasicTypeGetter {

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
        if (Objects.isNull(urlResource)) {
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
