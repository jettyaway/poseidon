package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.core.convert.Convert;
import com.voxlearning.poseidon.core.io.IOUtil;
import com.voxlearning.poseidon.core.io.resources.*;
import com.voxlearning.poseidon.core.io.watch.SimpleWatcher;
import com.voxlearning.poseidon.core.io.watch.WatcherMonitor;
import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.CharsetUtil;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组设置工具类
 * 1、支持变量，默认变量命名为 ${变量名}，变量只能识别读入行的变量，例如第6行的变量在第三行无法读取
 * 2、支持分组，分组为中括号括起来的内容，中括号以下的行都为此分组的内容，无分组相当于空字符分组<br>
 * 若某个key是name，加上分组后的键相当于group.name
 * 3、注释以#开头，但是空行和不带“=”的行也会被跳过，但是建议加#
 * 4、store方法不会保存注释内容，慎重使用
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class BasicSetting extends AbsSetting implements Map<Object, Object> {


    private static final long serialVersionUID = -5554198948563764955L;

    public static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    private final List<String> group = new LinkedList<>();

    private final Map<Object, Object> map = new ConcurrentHashMap<>();

    private Charset charset;

    private boolean isUseVariable;

    protected URL settingUrl;

    private WatcherMonitor watcherMonitor;

    private SettingLoader settingLoader;


    public BasicSetting() {

    }

    public BasicSetting(String path) {
        this(path, DEFAULT_CHARSET, false);
    }

    public BasicSetting(String path, Charset charset, boolean isUseVariable) {
        Preconditions.checkNotNull(path, "path can not be null or empty");
        init(ResourcesUtil.getResourceObj(path), charset, isUseVariable);
    }

    public BasicSetting(String path, Class<?> classLoader, Charset charset, boolean isUseVariable) {
        Preconditions.checkNotNull(path, "path can not be null or empty");
        init(new ClassPathResource(path, classLoader), charset, isUseVariable);
    }

    public BasicSetting(File configFile, Charset charset, boolean isUseVariable) {
        Preconditions.checkNotNull(configFile);
        init(new FileResource(configFile), charset, isUseVariable);
    }

    public BasicSetting(URL url, Charset charset, boolean isUseVariable) {
        Preconditions.checkNotNull(url);
        init(new URLResource(url), charset, isUseVariable);
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

    public synchronized boolean load() {
        if (Objects.nonNull(this.settingLoader)) {
            settingLoader = new SettingLoader(this, this.charset, this.isUseVariable);
        }
        return true;
    }

    /**
     * 自动加载
     *
     * @param autoLoad 是否自动加载
     */
    public void autoLoad(boolean autoLoad) {
        if (autoLoad) {
            if (Objects.nonNull(this.watcherMonitor)) {
                this.watcherMonitor.close();
            }
            watcherMonitor = WatcherMonitor.createMonitor(this.settingUrl, WatcherMonitor.MODIFY);
            watcherMonitor.setWatcher(new SimpleWatcher() {
                @Override
                public void onModify(WatchEvent<?> event, Path currentPath) {
                    load();
                }
            }).start();
        } else {
            IOUtil.close(this.watcherMonitor);
            this.watcherMonitor = null;
        }
    }

    /**
     * 获取配置文件的路径
     *
     * @return 路径
     */
    public String getSettingPath() {
        if (Objects.isNull(this.settingUrl)) {
            return null;
        }
        return this.settingUrl.getPath();
    }


    @Override
    public Optional<Object> getObj(String key, Object defaultValue) {
        Object value = map.get(key);
        if (Objects.isNull(value)) {
            return Optional.ofNullable(defaultValue);
        }
        return Optional.ofNullable(value);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }


    /**
     * 删除指定key中第一个value不为空的键对应的值
     *
     * @param keys 键
     * @return 第一个value不为空的值
     */
    public Object removeAndGet(String... keys) {
        Object value = null;
        for (String key : keys) {
            value = remove(key);
            if (Objects.nonNull(value)) {
                break;
            }
        }
        return value;
    }

    /**
     * 删除指定key中第一个value不为空的键对应值 的字符串类型
     *
     * @param keys 键
     * @return 第一个value不为空的值
     */
    public String removeAndGetStr(String... keys) {
        return (String) removeAndGet(keys);
    }

    /**
     * 获取自定分组的所有键值对
     *
     * @param group 分组名
     * @return 所有键值对
     */
    public Map<?, ?> getMap(String group) {
        if (StrUtil.isBlank(group)) {
            return this;
        }
        String groupDot = group.concat(StrUtil.DOT);
        Map<String, Object> map2 = new HashMap<>();
        String strKey;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            strKey = Convert.toStr(entry.getKey());
            if (Objects.nonNull(strKey) && strKey.startsWith(groupDot)) {
                map2.put(StrUtil.removePrefix(strKey, group), entry.getValue());
            }
        }
        return map2;
    }

    /**
     * 获取指定group对应的setting
     *
     * @param group 指定分组
     * @return {@link BasicSetting}
     */
    public BasicSetting getSetting(String group) {
        BasicSetting basicSetting = new BasicSetting();
        basicSetting.putAll(this.getMap(group));
        return basicSetting;
    }

    /**
     * @param group 获取对应分组的Properties
     * @return {@link Properties}
     * @see Properties
     */
    public Properties getProperties(String group) {
        Properties properties = new Properties();
        properties.putAll(getMap(group));
        return properties;
    }

    /**
     * 持久化当前的设置
     *
     * @param absPath 设置文件的绝对路径
     */
    public void store(String absPath) {
        if (Objects.isNull(settingLoader)) {
            settingLoader = new SettingLoader(this, this.charset, this.isUseVariable);
        }
        settingLoader.store(absPath);
    }

    /**
     * 设置变量的正则
     *
     * @param regex 正则字符串
     */
    public void setVarRegx(String regex) {
        if (Objects.isNull(regex)) {
            settingLoader = new SettingLoader(this, this.charset, this.isUseVariable);
        }
        settingLoader.setRegVar(regex);
    }

    /**
     * 将所有的配置转化为Properties,分区变为前缀
     *
     * @return
     */
    public Properties toProperties() {
        Properties properties = new Properties();
        properties.putAll(this.map);
        return properties;
    }

    /**
     * 获取所有的分组名
     *
     * @return
     */
    public List<String> getGroups() {
        return group;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return this.map.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return this.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<?, ?> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<Object> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicSetting that = (BasicSetting) o;
        return isUseVariable == that.isUseVariable &&
                Objects.equals(group, that.group) &&
                Objects.equals(map, that.map) &&
                Objects.equals(charset, that.charset) &&
                Objects.equals(settingUrl, that.settingUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, map, charset, isUseVariable, settingUrl);
    }

}
