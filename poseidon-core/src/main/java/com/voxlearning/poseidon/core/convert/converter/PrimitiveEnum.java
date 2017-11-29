package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.util.StrUtil;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public enum PrimitiveEnum {

    CHAR(char.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Character) {
                return ((Character) value).charValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return valueStr.charAt(0);
        }
    },

    BYTE(byte.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Byte) {
                return ((Byte) value).byteValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Byte.parseByte(valueStr);
        }
    },

    SHORT(short.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Number) {
                return ((Number) value).shortValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Short.parseShort(valueStr);
        }
    },

    INT(int.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Integer.parseInt(valueStr);
        }
    },

    LONG(long.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Long.parseLong(valueStr);
        }
    },

    FLOAT(float.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Float.parseFloat(valueStr);
        }
    },

    DOUBLE(double.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return Double.parseDouble(valueStr);
        }
    },

    BOOLEAN(boolean.class) {
        @Override
        Object getValue(Object value) {
            if (value instanceof Boolean) {
                return ((Boolean) value).booleanValue();
            }
            String valueStr = StrUtil.convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return 0;
            }
            return parseBoolean(valueStr);
        }
    };


    private Class<?> targetClass;

    PrimitiveEnum(Class<?> targetClass) {
        this.targetClass = targetClass;
    }


    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    private static Map<Class<?>, PrimitiveEnum> primitiveEnumMap;

    static {
        primitiveEnumMap = Stream.of(PrimitiveEnum.values()).collect(Collectors.toMap(PrimitiveEnum::getTargetClass, Function.identity()));
    }


    public static PrimitiveEnum fromClass(Class<?> clazz) {
        return primitiveEnumMap.get(clazz);
    }


    abstract Object getValue(Object value);

    static boolean parseBoolean(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        value = value.trim().toLowerCase();
        switch (value) {
            case "true":
                return true;
            case "false":
                return false;
            case "0":
                return false;
            case "1":
                return true;
            case "y":
                return true;
            case "n":
                return false;
            case "yes":
                return true;
            case "ok":
                return true;
            case "no":
                return false;
            default:
                return false;
        }
    }

}
