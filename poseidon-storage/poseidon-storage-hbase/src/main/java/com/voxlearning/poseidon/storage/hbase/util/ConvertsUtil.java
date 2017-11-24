package com.voxlearning.poseidon.storage.hbase.util;

import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.ObjectUtil;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ConvertsUtil {
    public ConvertsUtil() {
    }

    public static Object convert(Class type, String value) {
        return type.equals(String.class) ? value : (!type.equals(Integer.TYPE) && !type.equals(Integer.class) ? (!type.equals(Short.class) && !type.equals(Short.TYPE) ? (!type.equals(Integer.class) && !type.equals(Integer.TYPE) ? (!type.equals(Float.class) && !type.equals(Float.TYPE) ? (!type.equals(Double.class) && !type.equals(Double.TYPE) ? (!type.equals(Long.class) && !type.equals(Long.TYPE) ? (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE) ? null : (value == null ? Boolean.valueOf(false) : Boolean.valueOf(Boolean.parseBoolean(value)))) : (value == null ? Long.valueOf(0L) : Long.valueOf(Long.parseLong(value)))) : (value == null ? Double.valueOf(0.0D) : Double.valueOf(Double.parseDouble(value)))) : (value == null ? Float.valueOf(0.0F) : Float.valueOf(Float.parseFloat(value)))) : (value == null ? Integer.valueOf(0) : Integer.valueOf(Integer.parseInt(value)))) : (value == null ? Integer.valueOf(0) : Short.valueOf(Short.parseShort(value)))) : Integer.valueOf(Integer.parseInt(value)));
    }

    public static Object convertWithNull(Class type, String value) {
        return type.equals(String.class) ? value : (!type.equals(Integer.TYPE) && !type.equals(Integer.class) ? (!type.equals(Short.class) && !type.equals(Short.TYPE) ? (!type.equals(Integer.class) && !type.equals(Integer.TYPE) ? (!type.equals(Float.class) && !type.equals(Float.TYPE) ? (!type.equals(Double.class) && !type.equals(Double.TYPE) ? (!type.equals(Long.class) && !type.equals(Long.TYPE) ? (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE) ? (type.equals(Date.class) ? (value == null ? null : value.toString()) : (type.equals(java.util.Date.class) ? (value == null ? null : Long.valueOf(java.util.Date.parse(value))) : (type.equals(Timestamp.class) ? (value == null ? null : Timestamp.valueOf(value) + "") : null))) : (value == null ? null : Boolean.valueOf(Boolean.parseBoolean(value)))) : (value == null ? null : Long.valueOf(Long.parseLong(value)))) : (value == null ? null : Double.valueOf(Double.parseDouble(value)))) : (value == null ? null : Float.valueOf(Float.parseFloat(value)))) : (value == null ? null : Integer.valueOf(Integer.parseInt(value)))) : (value == null ? null : Short.valueOf(Short.parseShort(value)))) : Integer.valueOf(Integer.parseInt(value)));
    }

    public static Object bytesToValue(Type type, byte[] bts) {
        return bts == null && type.equals(String.class) ? null : (bts == null && !type.equals(String.class) ? null : (type.equals(String.class) ? Bytes.toString(bts) : (!type.equals(Integer.TYPE) && !type.equals(Integer.class) ? (!type.equals(Short.class) && !type.equals(Short.TYPE) ? (!type.equals(Float.class) && !type.equals(Float.TYPE) ? (!type.equals(Double.class) && !type.equals(Double.TYPE) ? (!type.equals(Long.class) && !type.equals(Long.TYPE) ? (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE) ? null : Boolean.valueOf(Bytes.toBoolean(bts))) : Long.valueOf(Bytes.toLong(bts))) : Double.valueOf(Bytes.toDouble(bts))) : Float.valueOf(Bytes.toFloat(bts))) : Short.valueOf(Bytes.toShort(bts))) : Integer.valueOf(Bytes.toInt(bts)))));
    }

    public static byte[] convertToBytes(Object obj) {
        return obj.getClass().equals(String.class) ? Bytes.toBytes(obj.toString()) : (!obj.getClass().equals(Integer.TYPE) && !obj.getClass().equals(Integer.class) ? (!obj.getClass().equals(Short.class) && !obj.getClass().equals(Short.TYPE) ? (!obj.getClass().equals(Integer.class) && !obj.getClass().equals(Integer.TYPE) ? (!obj.getClass().equals(Float.class) && !obj.getClass().equals(Float.TYPE) ? (!obj.getClass().equals(Double.class) && !obj.getClass().equals(Double.TYPE) ? (!obj.getClass().equals(Long.class) && !obj.getClass().equals(Long.TYPE) ? (!obj.getClass().equals(Boolean.class) && !obj.getClass().equals(Boolean.TYPE) ? null : (obj == null ? Bytes.toBytes(false) : Bytes.toBytes(Boolean.parseBoolean(obj.toString())))) : (obj.toString() == null ? Bytes.toBytes(0L) : Bytes.toBytes(Long.parseLong(obj.toString())))) : (obj == null ? Bytes.toBytes(0.0D) : Bytes.toBytes(Double.parseDouble(obj.toString())))) : (obj == null ? Bytes.toBytes(0.0F) : Bytes.toBytes(Float.parseFloat(obj.toString())))) : (obj == null ? Bytes.toBytes(0) : Bytes.toBytes(Integer.parseInt(obj.toString())))) : (obj == null ? Bytes.toBytes(0) : Bytes.toBytes(Short.parseShort(obj.toString())))) : Bytes.toBytes(Integer.parseInt(obj.toString())));
    }

    static Object fromBytes(byte[] bs, Class<?> type) {
        if (bs == null) {
            return null;
        }
        Preconditions.checkNotNull(type);

        if (type == byte[].class) {
            return bs;
        }
        if (type == BigDecimal.class) {
            return Bytes.toBigDecimal(bs);
        }
        if (type == Boolean.class) {
            return Bytes.toBoolean(bs);
        }
        if (type == Double.class) {
            return Bytes.toDouble(bs);
        }
        if (type == Float.class) {
            return Bytes.toFloat(bs);
        }
        if (type == Integer.class) {
            return Bytes.toInt(bs);
        }
        if (type == Long.class) {
            return Bytes.toLong(bs);
        }
        if (type == Short.class) {
            return Bytes.toShort(bs);
        }
        if (type == String.class) {
            return Bytes.toString(bs);
        }
        if (type == Character.class) {
            String s = Bytes.toString(bs);
            return s.charAt(0);
        }
        if (type == Date.class) {
            long t = Bytes.toLong(bs);
            return new Date(t);
        }
        return ObjectUtil.unserialize(bs);
    }

    static byte[] toBytes(Object value) {
        Preconditions.checkNotNull(value);
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof BigDecimal) {
            BigDecimal d = (BigDecimal) value;
            return Bytes.toBytes(d);
        }
        if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            return Bytes.toBytes(b);
        }
        if (value instanceof Double) {
            Double d = (Double) value;
            return Bytes.toBytes(d);
        }
        if (value instanceof Float) {
            Float f = (Float) value;
            return Bytes.toBytes(f);
        }
        if (value instanceof Integer) {
            Integer i = (Integer) value;
            return Bytes.toBytes(i);
        }
        if (value instanceof Long) {
            Long l = (Long) value;
            return Bytes.toBytes(l);
        }
        if (value instanceof Short) {
            Short s = (Short) value;
            return Bytes.toBytes(s);
        }
        if (value instanceof String) {
            String s = (String) value;
            return Bytes.toBytes(s);
        }
        if (value instanceof Character) {
            Character c = (Character) value;
            String s = c.toString();
            return Bytes.toBytes(s);
        }
        return ObjectUtil.serialize(value);
    }
}