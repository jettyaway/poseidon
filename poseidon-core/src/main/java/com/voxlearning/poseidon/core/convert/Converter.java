package com.voxlearning.poseidon.core.convert;

/**
 * 转化器接口　实现类型转换
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-28
 * @since 17-11-28
 */
public interface Converter<T> {

    /**
     * 转化为指定的类型<br/>
     * 如果类型无法确定，将读取默认值的类型
     *
     * @param value        原始值
     * @param defaultValue 　默认值
     * @param <T>          　返回类型
     * @return 转化后的类型值
     * @throws IllegalArgumentException
     */
    T convert(Object value, T defaultValue) throws IllegalArgumentException;
}
