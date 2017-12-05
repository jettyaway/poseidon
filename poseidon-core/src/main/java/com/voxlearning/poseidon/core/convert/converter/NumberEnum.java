package com.voxlearning.poseidon.core.convert.converter;

import com.voxlearning.poseidon.core.convert.ConvertException;
import com.voxlearning.poseidon.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基本数字类型枚举
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-11-29
 * @since 17-11-29
 */
public enum NumberEnum {

    SHORT(Short.class) {
        @Override
        Number getValue(Object value) {
            if ((value instanceof Number)) {
                return Short.valueOf(((Number) value).shortValue());
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return Short.valueOf(valueStr);
        }
    },

    BYTE(Byte.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return Byte.valueOf(((Number) value).byteValue());
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return Byte.valueOf(valueStr);

        }
    },

    INTEGER(Integer.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return Integer.valueOf(((Number) value).intValue());
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return Integer.valueOf(valueStr);

        }
    },
    ATOMIC_INTEGER(AtomicInteger.class) {
        @Override
        Number getValue(Object value) {
            int intValue;
            String valueStr = convertToString(value);
            if (StrUtil.isEmpty(valueStr)) {
                return null;
            }
            intValue = Integer.parseInt(valueStr);
            return new AtomicInteger(intValue);
        }
    },

    LONG(Long.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return Long.valueOf(((Number) value).longValue());
            }
            String valueStr = convertToString(value);
            if (Objects.isNull(valueStr)) {
                return null;
            }
            return Long.valueOf(valueStr);
        }
    },

    ATOMIC_LONG(AtomicLong.class) {
        @Override
        Number getValue(Object value) {
            long longValue;
            String valueStr = convertToString(value);
            if (StrUtil.isEmpty(valueStr)) {
                return null;
            }
            longValue = Long.parseLong(valueStr);
            return new AtomicLong(longValue);
        }
    },

    DOUBLE(Double.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return Double.valueOf(((Number) value).doubleValue());
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return Double.valueOf(valueStr);
        }
    },

    FLOAT(Float.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return Float.valueOf(((Number) value).floatValue());
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return Float.valueOf(valueStr);
        }
    },

    BIG_DECIMAL(BigDecimal.class) {
        @SuppressWarnings("unchecked")
        @Override
        Number getValue(Object value) {
            if (value instanceof Long) {
                return new BigDecimal((Long) value);
            } else if (value instanceof Integer) {
                return new BigDecimal((Integer) value);
            } else if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger) value);
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return new BigDecimal(valueStr);
        }
    },

    BIG_INTEGER(BigInteger.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Long) {
                return BigInteger.valueOf((Long) value);
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            return new BigInteger(valueStr);
        }
    },

    NUMBER(Number.class) {
        @Override
        Number getValue(Object value) {
            if (value instanceof Number) {
                return (Number) value;
            }
            String valueStr = convertToString(value);
            if (StrUtil.isBlank(valueStr)) {
                return null;
            }
            try {
                return NumberFormat.getInstance().parse(valueStr);
            } catch (ParseException e) {
                throw new ConvertException(e);
            }
        }
    };

    private Class<? extends Number> clazz;

    public Class<? extends Number> getClazz() {
        return this.clazz;
    }

    NumberEnum(Class<? extends Number> clazz) {
        this.clazz = clazz;
    }

    private static Map<Class<? extends Number>, NumberEnum> numberEnumMap;

    static {
        numberEnumMap = Stream.of(NumberEnum.values())
                .collect(Collectors.toMap(NumberEnum::getClazz, Function.identity()));
    }

    public static NumberEnum fromClass(Class<? extends Number> clazz) {
        return numberEnumMap.get(clazz);
    }

    abstract Number getValue(Object value);

    public String convertToString(Object value) {
        String valueStr = StrUtil.convertToString(value);
        return Objects.isNull(valueStr) ? null : valueStr.trim();
    }


}
