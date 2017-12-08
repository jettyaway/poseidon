package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.core.io.resources.Resources;
import com.voxlearning.poseidon.core.io.resources.ResourcesUtil;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.CharsetUtil;
import com.voxlearning.poseidon.settings.expception.SettingException;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组设置工具类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class BasicSetting extends AbsSetting implements Map<Object, Object> {


    private static final long serialVersionUID = -5554198948563764955L;

    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    private final List<String> group = new LinkedList<>();

    private final Map<Object, Object> map = new ConcurrentHashMap<>();

    private Charset charset;

    private boolean isUseVariable;

    protected URL settingUrl;

    private WatcherMonitor watcherMonitor;


    public BasicSetting() {

    }

    public BasicSetting(String path) {
        this(path, DEFAULT_CHARSET, false);
    }

    public BasicSetting(String path, Charset charset, boolean isUseVariable) {
        Preconditions.checkNotNull(path, "path can not be null or empty");
        init(ResourcesUtil.getResourceObj(path), charset, isUseVariable);
    }

    public List<String> getGroup() {
        return this.group;
    }

    public boolean init(Resources resource, Charset charset, boolean isUseVariable) {
        if (Objects.isNull(resource)) {
            throw new NullPointerException("Null setting url define.");
        }
        this.settingUrl = resource.getURL();
        this.isUseVariable = isUseVariable;
        this.charset = charset;
        return load();
    }

    public boolean load() {
        return true;
    }


    @Override
    public Optional<Object> getObj(String key, Object defaultValue) {
        return Optional.empty();
    }

    @Override
    public Optional<Character> getChar(String key, Character defaultValue) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(Object key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<?, ?> m) {

    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<Object> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return null;
    }
}
