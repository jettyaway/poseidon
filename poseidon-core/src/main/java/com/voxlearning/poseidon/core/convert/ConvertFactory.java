package com.voxlearning.poseidon.core.convert;


import com.voxlearning.poseidon.core.convert.converter.NumberConverter;
import com.voxlearning.poseidon.core.convert.converter.PrimitiveConverter;
import com.voxlearning.poseidon.core.util.ArrayUtil;
import com.voxlearning.poseidon.core.util.ReflectUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public class ConvertFactory {

    private static final ConvertFactory INSTANCE = new ConvertFactory();

    private Map<Type, Converter<?>> defaultConvertMap;

    private volatile Map<Type, Converter<?>> customConvertMap;

    private ConvertFactory() {
        registConvert();
    }

    public static ConvertFactory getInstance() {
        return INSTANCE;
    }

    public void putCustom(Type type, Class<? extends Converter<?>> converterClass) {
        putCustom(type, ReflectUtil.newInstance(converterClass));
    }

    public void putCustom(Type type, Converter<?> convert) {
        if (Objects.isNull(customConvertMap)) {
            synchronized (this) {
                if (Objects.isNull(customConvertMap)) {
                    customConvertMap = new ConcurrentHashMap<>();
                }
            }
        }
        customConvertMap.put(type, convert);
    }


    public <T> Converter<T> getConverter(Type type, boolean isCustomFirst) {
        Converter<T> converter = null;
        if (isCustomFirst) {
            converter = getCustomConverter(type);
            if (Objects.nonNull(converter)) {
                return converter;
            }
            return getDefaultConverter(type);
        }
        converter = getDefaultConverter(type);
        if (Objects.nonNull(converter)) {
            return converter;
        }
        return getCustomConverter(type);

    }

    @SuppressWarnings("unchecked")
    public <T> Converter<T> getCustomConverter(Type type) {
        return Objects.isNull(customConvertMap) ? null : (Converter<T>) this.customConvertMap.get(type);
    }

    @SuppressWarnings("unchecked")
    public <T> Converter<T> getDefaultConverter(Type type) {
        return Objects.isNull(defaultConvertMap) ? null : (Converter<T>) this.defaultConvertMap.get(type);
    }

    /**
     * 转化为指定类型<br/>
     * 自定义转化器优先
     *
     * @param type         目标类型
     * @param value        　转化值
     * @param defaultValue 默认值
     * @param <T>          泛型类型
     * @return 转换后的值
     * @throws ConvertException 转换器不存在时抛出
     */
    public <T> T convert(Class<T> type, Object value, T defaultValue) throws ConvertException {
        return convert(type, value, defaultValue, true);
    }

    public <T> T convert(Class<T> type, Object value) throws ConvertException {
        return convert(type, value, null);
    }


    /**
     * 转化为指定类型
     *
     * @param type          　类型
     * @param value         　值
     * @param defaultValue  　默认值
     * @param isCustomFirst 　是否自定义转换器优先
     * @param <T>           目标类型
     * @return 转化后的值
     * @throws ConvertException 　转化器不存在时抛出
     */
    @SuppressWarnings("unchecked")
    public <T> T convert(Class<T> type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException {
        if (Objects.isNull(type) && Objects.isNull(defaultValue)) {
            throw new NullPointerException("[type] and [defaultValue] both null ,can not know what type to convert.");
        }
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        if (Objects.isNull(type)) {
            type = (Class<T>) defaultValue.getClass();
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        Class<?> valueClass = value.getClass();
        if (type.isArray() && valueClass.isArray()) {
            try {
                return (T) ArrayUtil.cast(type, value);
            } catch (Exception e) {

            }
        }
        Converter<T> converter = getConverter(type, isCustomFirst);
        if (Objects.nonNull(converter)) {
            return converter.convert(value, defaultValue);
        }
        //TODO 如果type 是BEAN类型,并且value是map 类型,那么尝试用MAP填充BEAN
        throw new ConvertException("No converter for type[%s]", type.getName());
    }

    private void registConvert() {
        defaultConvertMap = new HashMap<>();
        defaultConvertMap.put(char.class, new PrimitiveConverter(char.class));
        defaultConvertMap.put(byte.class, new PrimitiveConverter(byte.class));
        defaultConvertMap.put(short.class, new PrimitiveConverter(short.class));
        defaultConvertMap.put(int.class, new PrimitiveConverter(int.class));
        defaultConvertMap.put(long.class, new PrimitiveConverter(long.class));
        defaultConvertMap.put(float.class, new PrimitiveConverter(float.class));
        defaultConvertMap.put(double.class, new PrimitiveConverter(double.class));
        defaultConvertMap.put(boolean.class, new PrimitiveConverter(boolean.class));

        defaultConvertMap.put(Number.class, new NumberConverter(Number.class));
        defaultConvertMap.put(Byte.class, new NumberConverter(Byte.class));
        defaultConvertMap.put(Short.class, new NumberConverter(Short.class));
        defaultConvertMap.put(Integer.class, new NumberConverter(Integer.class));
        defaultConvertMap.put(Long.class, new NumberConverter(Long.class));
        defaultConvertMap.put(Float.class, new NumberConverter(Float.class));
        defaultConvertMap.put(Double.class, new NumberConverter(Double.class));
        defaultConvertMap.put(AtomicInteger.class, new NumberConverter(AtomicInteger.class));
        defaultConvertMap.put(AtomicLong.class, new NumberConverter(AtomicLong.class));
        defaultConvertMap.put(BigInteger.class, new NumberConverter(BigInteger.class));
        defaultConvertMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
    }


}
