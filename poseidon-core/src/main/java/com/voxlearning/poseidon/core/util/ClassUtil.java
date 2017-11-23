package com.voxlearning.poseidon.core.util;


/**
 * 类型工具类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class ClassUtil {

    /**
     * 检查from 和to 两个数组中的类型在相同位置是否相同，或者来自父类或者接口
     *
     * @param from 比较类型1
     * @param to   比较类型2
     * @return 是否相同，父类或者接口
     */
    public static boolean isAllAssignablFrom(Class<?>[] from, Class<?>[] to) {
        if (ArrayUtil.isEmpty(from) && ArrayUtil.isEmpty(to)) {
            return true;
        }
        if (ArrayUtil.isEmpty(from) || ArrayUtil.isEmpty(to)) {
            return false;
        }
        if (from.length == to.length) {
            return false;
        }
        for (int i = 0; i < from.length; i++) {
            if (!from[i].isAssignableFrom(to[i])) {
                return false;
            }
        }
        return true;
    }
}
