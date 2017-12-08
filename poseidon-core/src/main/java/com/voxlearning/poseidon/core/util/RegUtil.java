package com.voxlearning.poseidon.core.util;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class RegUtil {


    /**
     * 获取匹配的所有结果
     *
     * @param regex      正则字符串
     * @param content    内容
     * @param group      分组
     * @param collection 返回的集合
     * @param <T>        集合 类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(String regex, String content, int group, T collection) {
        if (StrUtil.isBlank(regex)) {
            return null;
        }
        Pattern compile = Pattern.compile(regex, Pattern.DOTALL);
        return findAll(compile, content, group, collection);
    }


    /**
     * 获取匹配的所有结果
     *
     * @param pattern    正则
     * @param content    内容
     * @param group      分组
     * @param collection 返回的集合
     * @param <T>        集合 类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(Pattern pattern, String content, int group, T collection) {
        if (Objects.isNull(pattern) || Objects.isNull(content)) {
            return null;
        }
        if (Objects.isNull(collection)) {
            throw new NullPointerException("return collection can not be null.");
        }
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            collection.add(matcher.group(group));
        }
        return collection;
    }

    /**
     * 获得匹配的字符串
     *
     * @param regex   正则模式
     * @param content 被匹配的内容
     * @param group   匹配正则的分组序号
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(String regex, String content, int group) {
        if (StrUtil.isBlank(regex)) {
            return null;
        }
        Pattern compile = Pattern.compile(regex, Pattern.DOTALL);
        return get(compile, content, group);
    }

    /**
     * 获得匹配的字符串
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @param group   匹配正则的分组序号
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(Pattern pattern, String content, int group) {
        if (Objects.isNull(pattern) || Objects.isNull(content)) {
            return null;
        }
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(group);

        }
        return null;
    }
}
