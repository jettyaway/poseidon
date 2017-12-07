package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.core.bean.BeanUtil;
import com.voxlearning.poseidon.core.convert.Convert;
import com.voxlearning.poseidon.core.getter.OptNullBasicTypeFromObjectGetter;
import com.voxlearning.poseidon.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-07
 * @since 17-12-7
 */
public abstract class AbsSetting extends OptNullBasicTypeFromObjectGetter<String> implements Serializable {

    private static final long serialVersionUID = 2691244284790175658L;

    private static final Logger logger = LoggerFactory.getLogger(AbsSetting.class);

    public static final String ARRAY_DEFAULT_DELIMITER = ",";

    @Override
    public abstract Optional<Object> getObj(String key, Object defaultValue);

    /**
     * 获取指定组下面的字符串值
     *
     * @param key          KEY
     * @param group        分组
     * @param defaultValue 默认值
     * @return KEY对应的值 如果不存在则返回默认值
     */
    public String getStr(String key, String group, String defaultValue) {
        String value = getByGroup(key, group);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    public String getByGroup(String key, String group) {
        return getStr(keyWithGroup(key, group)).orElse("");
    }

    public String getWithLog(String key) {
        String value = getStr(key).orElse("");
        if (StrUtil.isBlank(value)) {
            logger.debug("No key define for [{}]", key);
        }
        return value;
    }

    public String getByGroupWithLog(String key, String group) {
        String value = getStr(keyWithGroup(key, group)).orElse("");
        if (StrUtil.isBlank(value)) {
            logger.debug("No key define for [{}]", key);
        }
        return value;
    }

    public String[] getStrings(String key, String group, String delimiter) {
        String value = getByGroup(key, group);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return StrUtil.split(value, delimiter);
    }

    public String[] getStrings(String key, String group) {
        return getStrings(key, group, ARRAY_DEFAULT_DELIMITER);
    }

    public Integer getInt(String key, String group) {
        return getInt(key, group, null);
    }

    public Integer getInt(String key, String group, Integer defaultValue) {
        return Convert.toInt(getByGroup(key, group), defaultValue);
    }

    public Boolean getBoolean(String key, String group, Boolean defaultValue) {
        return Convert.toBoolean(getByGroup(key, group), defaultValue);
    }

    public Boolean getBoolean(String key, String group) {
        return getBoolean(key, group, null);
    }

    public Long getLong(String key, String group, Long defaultVaule) {
        return Convert.toLong(getByGroup(key, group), defaultVaule);
    }

    public Long getLong(String key, String group) {
        return getLong(key, group, null);
    }

    public Character getChar(String key, String group, Character defaultValue) {
        return Convert.toChar(getByGroup(key, group), defaultValue);
    }

    public Character getChar(String key, String group) {
        return getChar(key, group, null);
    }

    public Float getFloat(String key, String group, Float defaultValue) {
        return Convert.toFloat(getByGroup(key, group), defaultValue);
    }

    public Float getFloat(String key, String group) {
        return getFloat(key, group, null);
    }

    public Double getDouble(String key, String group, Double defauleValue) {
        return Convert.toDouble(getByGroup(key, group), defauleValue);
    }

    public Double getDouble(String key, String group) {
        return getDouble(key, group, null);
    }


    /**
     * 将setting中键值对映射到bean 中 调用setter方法
     * 只支持基本类型的转化
     *
     * @param group 配置组
     * @param bean  Bean对象
     * @return bean
     */
    public Object toBean(final String group, Object bean) {
        return BeanUtil.fillBean(bean, new BeanUtil.ValueProvider<String>() {
            @Override
            public Object value(String key, Type valueType) {
                return null;
            }

            @Override
            public boolean containsKey(String key) {
                return false;
            }
        }, BeanUtil.CopyOptions.create());
    }

    /**
     * 将setting中键值对映射到bean 中 调用setter方法
     * 只支持基本类型的转化
     *
     * @param bean Bean对象
     * @return bean
     */
    public Object toBean(Object bean) {
        return toBean(null, bean);
    }


    /**
     * 更具 key 和 group 生成新key
     *
     * @param key   键
     * @param group 分组
     * @return 新key
     */
    private String keyWithGroup(String key, String group) {
        String keyWithGroup = key;
        if (StrUtil.isNotBlank(group)) {
            keyWithGroup = group.concat(StrUtil.DOT).concat(key);
        }
        return keyWithGroup;
    }
}
