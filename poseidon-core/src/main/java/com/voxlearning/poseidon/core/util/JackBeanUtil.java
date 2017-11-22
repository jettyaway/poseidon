package com.voxlearning.poseidon.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JackBeanUtil {
    public JackBeanUtil() {
    }

    public static List<Field> getBeanFieldsByClass(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field[] var2 = fields;
        int var3 = fields.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Field var10000 = var2[var4];
        }

        return Arrays.asList(fields);
    }

    public static Object getProperty(Object bean, String name) {
        Object value = null;
        String firstLetter = name.substring(0, 1).toUpperCase();
        String methodName = "get" + firstLetter + name.substring(1);
        Method method = null;

        try {
            method = bean.getClass().getMethod(methodName, new Class[0]);
            value = method.invoke(bean, new Object[0]);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return value;
    }

    public static void setProperty(Object bean, String name, Object value) {
        String firstLetter = name.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + name.substring(1);
        Method method = null;

        try {
            Method[] methods = bean.getClass().getDeclaredMethods();
            Method[] var7 = methods;
            int var8 = methods.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Method method1 = var7[var9];
                if (setMethodName.equals(method1.getName())) {
                    method = method1;
                    break;
                }
            }

            method.invoke(bean, new Object[]{value});
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }

    public static <T> T getInstanceByClass(Class<T> clazz) {
        T model = null;

        try {
            model = clazz.newInstance();
        } catch (InstantiationException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }

        return model;
    }
}
