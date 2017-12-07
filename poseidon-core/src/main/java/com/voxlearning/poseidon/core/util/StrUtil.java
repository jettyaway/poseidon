package com.voxlearning.poseidon.core.util;

import com.voxlearning.poseidon.core.lang.StrSpliter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-23
 * @since 17-11-23
 */
public class StrUtil {

    public static final int INDEX_NOT_FOUND = -1;
    public static final char C_SPACE = ' ';
    public static final char C_TAB = '	';
    public static final char C_DOT = '.';
    public static final char C_SLASH = '/';
    public static final char C_BACKSLASH = '\\';
    public static final char C_CR = '\r';
    public static final char C_LF = '\n';
    public static final char C_UNDERLINE = '_';
    public static final char C_COMMA = ',';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';
    public static final char C_BRACKET_START = '[';
    public static final char C_BRACKET_END = ']';
    public static final char C_COLON = ':';

    public static final String SPACE = " ";
    public static final String TAB = "	";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String BRACKET_START = "[";
    public static final String BRACKET_END = "]";
    public static final String COLON = ":";

    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";

    public static final String EMPTY_JSON = "{}";

    /**
     * <p>Returns either the passed in String,
     * or if the String is {@code null}, an empty String ("").</p>
     * <p>
     * <pre>
     * StringUtils.defaultString(null)  = ""
     * StringUtils.defaultString("")    = ""
     * StringUtils.defaultString("bat") = "bat"
     * </pre>
     *
     * @param str the String to check, may be null
     * @return the passed in String, or the empty String if it
     * was {@code null}
     * @see String#valueOf(Object)
     */
    public static String defaultString(final String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * <p>Returns either the passed in String, or if the String is
     * {@code null}, the value of {@code defaultStr}.</p>
     * <p>
     * <pre>
     * StringUtils.defaultString(null, "NULL")  = "NULL"
     * StringUtils.defaultString("", "NULL")    = ""
     * StringUtils.defaultString("bat", "NULL") = "bat"
     * </pre>
     *
     * @param str        the String to check, may be null
     * @param defaultStr the default String to return
     *                   if the input is {@code null}, may be null
     * @return the passed in String, or the default if it was {@code null}
     * @see String#valueOf(Object)
     */
    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }


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
        return !isBlank(sequence);
    }


    public static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }


    public static boolean isNotEmpty(CharSequence sequence) {
        return !isEmpty(sequence);
    }

    /**
     * 字符串比较
     *
     * @param sequence1
     * @param sequence2
     * @return
     */
    public static boolean equalsIgnoreCase(CharSequence sequence1, CharSequence sequence2) {
        return equals(sequence1, sequence2, true);
    }

    /**
     * 字符串比较
     *
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

    /**
     * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
     * position: the first {@code %s} gets {@code args[0]}, etc.  If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the formatted message in
     * square braces.
     *
     * @param template a non-null string containing 0 or more {@code %s} placeholders.
     * @param args     the arguments to be substituted into the message template. Arguments are converted
     *                 to strings using {@link String#valueOf(Object)}. Arguments can be null.
     */
    // Note that this is somewhat-improperly used from Verify.java as well.
    public static String format(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str(str);
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return subSuf(str2, prefix.length());// 截取后半段
        }
        return str2;
    }

    /**
     * 去掉指定的前缀
     *
     * @param path   　字符串
     * @param prefix 　前缀
     * @return 截取后的字符串，如果不是prefix 开头　返回原字符串
     */
    public static String removePrefix(CharSequence path, CharSequence prefix) {
        if (isEmpty(path) || isEmpty(prefix)) {
            return str(path);
        }
        String str = path.toString();
        if (str.startsWith(prefix.toString())) {
            return subSuf(str, prefix.length());
        }
        return str;
    }

    /**
     * 切割后部分
     *
     * @param string    字符串
     * @param fromIndex 切割开始的位置（包括）
     * @return 切割后的字符串
     */
    public static String subSuf(CharSequence string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param string    String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return string.toString().substring(fromIndex, toIndex);
    }

    /**
     * 字符串是否以给定字符开始
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否开始
     */
    public static boolean startWith(CharSequence str, char c) {
        return c == str.charAt(0);
    }

    /**
     * 是否以指定字符串开头
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWith(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false);
    }

    /**
     * 是否以指定字符串开头，忽略大小写
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, true);
    }

    /**
     * 给定字符串是否以任何一个字符串开始<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @return 给定字符串是否以任何一个字符串开始
     * @since 3.0.6
     */
    public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否以指定字符串开头<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str          被监测字符串
     * @param prefix       开头字符串
     * @param isIgnoreCase 是否忽略大小写
     * @return 是否以指定字符串开头
     */
    public static boolean startWith(CharSequence str, CharSequence prefix, boolean isIgnoreCase) {
        if (null == str || null == prefix) {
            if (null == str && null == prefix) {
                return true;
            }
            return false;
        }

        if (isIgnoreCase) {
            return str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
        } else {
            return str.toString().startsWith(prefix.toString());
        }
    }

    /**
     * 将任意对象转化为字符串
     *
     * @param value 　需要转化的对象
     * @return {@link String}
     */
    public static String convertToString(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        } else if (ArrayUtil.isArray(value)) {
            return ArrayUtil.toString(value);
        }
        return value.toString();
    }

    /**
     * 将字符串转化为字节数组
     *
     * @param value   字符串
     * @param charset 编码
     * @return 字节数组
     */
    public static byte[] bytes(CharSequence value, Charset charset) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            return value.toString().getBytes();
        }
        return value.toString().getBytes(charset);
    }


    public static String str(Byte[] data, Charset charset) {
        if (Objects.isNull(data)) {
            return null;
        }
        byte[] bytes = new byte[data.length];
        Byte byteValue;
        for (int i = 0; i < data.length; i++) {
            byteValue = data[i];
            bytes[i] = Objects.isNull(byteValue) ? -1 : byteValue.byteValue();
        }
        return str(bytes, charset);
    }

    public static String str(Byte[] data, String charsetName) {
        return str(data, StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }

    public static String str(byte[] data, String charsetName) {
        return str(data, StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }


    /**
     * 将byte数组转化为字符串
     *
     * @param data    byte数组
     * @param charset 编码格式
     * @return {@link String}
     */
    public static String str(byte[] data, Charset charset) {
        if (Objects.isNull(data)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            return new String(data);
        }
        return new String(data, charset);
    }


    public static String str(ByteBuffer byteBuffer, String charsetName) {
        return str(byteBuffer, StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }

    /**
     * 将byteBuffer转化为String
     *
     * @param byteBuffer 需要转化的对象
     * @param charset    编码格式
     * @return 转化后的字符串
     * @see String
     */
    public static String str(ByteBuffer byteBuffer, Charset charset) {
        if (Objects.isNull(byteBuffer)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(byteBuffer).toString();
    }

    /**
     * 将对象转化为字符串
     *
     * @param obj     对象
     * @param charset 编码格式
     * @return
     */
    public static String str(Object obj, Charset charset) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str(obj, charset);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.toString(obj);
        }
        return obj.toString();
    }

    /**
     * 转化为指定编码的字符串
     *
     * @param obj         对象
     * @param charsetName 编码名称
     * @return 转化后的字符串
     */
    public static String str(Object obj, String charsetName) {
        return str(obj, StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }

    /**
     * object 转字符串
     * 格式为UTF-8
     *
     * @param obj 对象
     * @return 转化后的字符串
     */
    public static String utf8Str(Object obj) {
        return str(obj, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置，如果小于0，从0开始查找
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar, int start) {
        if (str instanceof String) {
            return ((String) str).indexOf(searchChar, start);
        } else {
            return indexOf(str, searchChar, start, -1);
        }
    }

    /**
     * 指定范围内查找指定字符
     *
     * @param str        字符串
     * @param searchChar 被查找的字符
     * @param start      起始位置，如果小于0，从0开始查找
     * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
     * @return 位置
     */
    public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
        final int len = str.length();
        if (start < 0 || start > len) {
            start = 0;
        }
        if (end > len || end < 0) {
            end = len;
        }
        for (int i = start; i < end; i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定范围内查找字符串
     * <p>
     * <pre>
     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内反向查找字符串
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置
     * @param ignoreCase 是否忽略大小写
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOf(final CharSequence str, final CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }

        final int endLimit = str.length() - searchStr.length() + 1;
        if (fromIndex > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return fromIndex;
        }

        if (false == ignoreCase) {
            //不忽略大小写调用JDK方法
            return str.toString().indexOf(searchStr.toString(), fromIndex);
        }

        for (int i = fromIndex; i < endLimit; i++) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }


    // ----------------------------------------------------------------------------------SPLIT

    /**
     * 切分字符串
     * a#b#c =>[a,b,c]<br/>
     * a##b#c => [a,"",b,c]
     *
     * @param value 被切分的字符串
     * @param split 分隔的字符串
     * @return 切分后的集合
     */
    public static List<String> split(CharSequence value, char split) {
        return split(value, split, 0);
    }

    /**
     * 切分字符串，不去除切分后每个元素两边的空白符，不去除空白项
     * a#b#c =>[a,b,c]<br/>
     * a##b#c => [a,"",b,c]
     *
     * @param value 被切分的字符串
     * @param split 分隔的字符串
     * @param limit 限制分片数，-1不限制
     * @return 切分后的集合
     */
    public static List<String> split(CharSequence value, char split, int limit) {
        return split(value, split, limit, false, false);
    }

    public static List<String> split(CharSequence value, char split, boolean isTrim, boolean ignoreEmpty) {
        return split(value, split, 0, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串为list
     *
     * @param value       字符串
     * @param split       分割字符
     * @param limit       限制分片数，-1不限制
     * @param isTrim      是否去除切分后字符串两边的空格
     * @param ignoreEmpty 是否区分大小写
     * @return 切分后字符串的结合
     */
    public static List<String> split(CharSequence value, char split, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (Objects.isNull(value)) {
            return new ArrayList<>(0);
        }
        return StrSpliter.split(value.toString(), split, limit, isTrim, ignoreEmpty);
    }


    /**
     * 切分字符串为数组形式
     *
     * @param value     字符串
     * @param separator 分隔符
     * @return 字符串数组
     */
    public static String[] split(CharSequence value, CharSequence separator) {
        if (Objects.isNull(value)) {
            return new String[]{};
        }
        String separatorStr = Objects.isNull(separator) ? null : separator.toString();
        return StrSpliter.splitToArray(value.toString(), separatorStr, 0, false, false);
    }


    /**
     * 给定字符串数组全部做去首尾空格<br/>
     * 如果为空则返回<code>null</code>
     *
     * @param str 字符串
     * @return 去掉空格后的字符串
     */
    public static String trimToNull(CharSequence str) {
        final String trimStr = trim(str);
        return EMPTY.equals(trimStr) ? null : trimStr;
    }

    /**
     * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     *
     * @param str 字符串
     * @return 去掉空格后的字符串
     */
    public static String trim(CharSequence str) {
        return (null == str) ? null : trim(str, 0);
    }

    /**
     * 给定字符串数组全部做去首尾空格
     *
     * @param strs 字符串数组
     */
    public static void trim(String[] strs) {
        if (null == strs) {
            return;
        }
        String str;
        for (int i = 0; i < strs.length; i++) {
            str = strs[i];
            if (null != str) {
                strs[i] = str.trim();
            }
        }
    }

    /**
     * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     *
     * @param str  要处理的字符串
     * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(CharSequence str, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        // 扫描字符串头部
        if (mode <= 0) {
            while ((start < end) && (NumberUtil.isBlankChar(str.charAt(start)))) {
                start++;
            }
        }

        // 扫描字符串尾部
        if (mode >= 0) {
            while ((start < end) && (NumberUtil.isBlankChar(str.charAt(end - 1)))) {
                end--;
            }
        }

        if ((start > 0) || (end < length)) {
            return str.toString().substring(start, end);
        }

        return str.toString();
    }

    /**
     * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get =》 return getName
     *
     * @param str       被处理的字符串
     * @param preString 添加的首部
     * @return 处理后的字符串
     */
    public static String upperFirstAndAddPre(CharSequence str, String preString) {
        if (str == null || preString == null) {
            return null;
        }
        return preString + upperFirst(str);
    }

    /**
     * 大写首字母<br>
     * 例如：str = name, return Name
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperFirst(CharSequence str) {
        if (null == str) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + subSuf(str, 1);
            }
        }
        return str.toString();
    }

    /**
     * 将下划线("_")转化为驼峰命名
     * example: get_name => getName
     *
     * @param name 需要转化的字符串
     * @return 转化后的字符串传
     */
    public static String toCamelCase(CharSequence name) {
        if (Objects.isNull(name)) {
            return null;
        }
        String name2 = name.toString();
        if (!name2.contains(UNDERLINE)) {
            return name2;
        }
        name2 = name2.toLowerCase();
        String[] names = name2.split(UNDERLINE);
        StringBuilder stringBuilder = new StringBuilder();
        boolean upper = false;
        for (int i = 0; i < names.length; i++) {
            char c = name.charAt(i);
            if (UNDERLINE.equals(c)) {
                upper = true;
            } else if (upper) {
                stringBuilder.append(Character.toUpperCase(c));
                upper = false;
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
     * 例如：HelloWorld=》hello_world
     *
     * @param camelCaseStr 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String toUnderlineCase(CharSequence camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }

        final int length = camelCaseStr.length();
        StringBuilder sb = new StringBuilder();
        char c;
        boolean isPreUpperCase = false;
        for (int i = 0; i < length; i++) {
            c = camelCaseStr.charAt(i);
            boolean isNextUpperCase = true;
            if (i < (length - 1)) {
                isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
            }
            if (Character.isUpperCase(c)) {
                if (!isPreUpperCase || !isNextUpperCase) {
                    if (i > 0) {
                        sb.append(UNDERLINE);
                    }
                }
                isPreUpperCase = true;
            } else {
                isPreUpperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 生成getter方法
     *
     * @param fieldName 字段名字
     * @return
     */
    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }


    /**
     * 生成setter方法
     *
     * @param fieldName 字段名字
     * @return
     */
    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

}


