package com.voxlearning.storage.core.util;


import com.voxlearning.poseidon.api.annotation.HbaseDocument;
import com.voxlearning.poseidon.api.annotation.HbaseRowKey;
import com.voxlearning.poseidon.core.util.AnnoatationsUtil;

public class HbaseAnnontationUtil {


    public static String getTableName(Class<?> clazz) {
        return clazz.getAnnotation(HbaseDocument.class).table();
    }


    public static String getFamilyName(Class<?> clazz) {
        return clazz.getAnnotation(HbaseDocument.class).family();
    }


    public static String getRowKeyName(Class clazz) {
        return AnnoatationsUtil.getFieldNameByFieldAnnoatation(clazz, HbaseRowKey.class);
    }


    public static <T> Object getRowKeyValue(T model) {
        return AnnoatationsUtil.getFieldValueByFieldAnnonation(model, HbaseRowKey.class);
    }

}
