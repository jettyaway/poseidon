package com.voxlearning.poseidon.core.util;

import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class StrUtil {

    public static boolean isBlank(CharSequence sequence) {
        int length;

        if ((sequence == null) || ((length = sequence.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            char c = sequence.charAt(i);
            if (false == (Character.isWhitespace(c) || Character.isSpaceChar(c))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param sequence 要判断的字符串
     * @return 如果为<code>null</code> 或者长度等于0 或者为空格 返回true 否则返回false
     */
    public static boolean isNotBlank(CharSequence sequence) {
        return isBlank(sequence);
    }

    /**
     * @param sequence1  要比较的字符串
     * @param sequence2  要比较的字符串
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串equals或者为<code>null</code> 则返回<code>true</code>
     */
    public static boolean equals(CharSequence sequence1, CharSequence sequence2, boolean ignoreCase) {
        if (Objects.isNull(sequence1)) {
            return Objects.isNull(sequence2);
        }
        if (Objects.isNull(sequence2)) {
            return Objects.isNull(sequence1);
        }
        if (ignoreCase) {
            return sequence1.toString().equalsIgnoreCase(sequence2.toString());
        }
        return sequence1.toString().equals(sequence2.toString());
    }
}
