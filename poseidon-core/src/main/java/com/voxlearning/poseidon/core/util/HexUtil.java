package com.voxlearning.poseidon.core.util;

import java.nio.charset.Charset;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-30
 * @since 17-11-30
 */
public class HexUtil {


    /**
     * 十六进制字符常量 小写
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


    /**
     * 十六进制字符常量　大写
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    /**
     * 判断给定的额字符串是否为十六进制
     *
     * @param value 值
     * @return true or false {@link Boolean}
     */
    public static boolean isHexNumber(String value) {
        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x") || value.startsWith("0X") || value.startsWith("#");
    }

    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, false);
    }

    public static char[] encodeHex(String str, Charset charset) {
        return encodeHex(StrUtil.bytes(str, charset), false);
    }


    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data);
    }

    public static String encodeHexStr(String data, Charset charset) {
        return encodeHexStr(StrUtil.bytes(data, charset), false);
    }

    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * @param data     bytes[]数组
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制 {@link String}
     * @see #encodeHexStr(byte[], char[])
     */
    private static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }


    /**
     * 将字节数组转化为十六进制数组
     * 一个byte占8位 十六进制每个字符需要4位二进制位来表示
     * 所以可以通过分别求得一个byte的高低四位的值然后映射为对应的{@link Character} 求得
     *
     * @param data     byte数组
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    private static char[] encodeHex(byte[] data, char[] toDigits) {
        char[] out = new char[data.length << 1];
        for (int i = 0, j = 0; i < data.length; i++) {
            //高四位
            out[j++] = toDigits[0XF0 & data[i] >>> 4];
            //低四位
            out[j++] = toDigits[0X0F & data[i]];
        }
        return out;
    }



    /**
     * 将一个字符数组转化为字符数组
     *
     * @param hexData 十六进制字符数组
     * @return 字节数组
     */
    public static byte[] decodeHex(char[] hexData) {
        int len = hexData.length;
        //如果char数组的长度为奇数,则不合法
        if ((len & 0X01) != 0) {
            throw new RuntimeException("your data is illegal.");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; i < len; i++) {
            //高四位
            int f = toDigit(hexData[j], j) << 4;
            j++;
            //与上低四位 相当于连接两个byte数组
            f = f | toDigit(hexData[j], j);
            //转化为byte
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * 将一个字符转化为16进制整数值
     *
     * @param ch    字符
     * @param index 索引
     * @return 转化后的整数
     */
    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == 1) {
            throw new RuntimeException("Illegal hex character " + ch + " at index" + index);
        }
        return digit;
    }

}
