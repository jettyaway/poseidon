package com.voxlearning.poseidon.storage.hbase.client.impl;

import com.voxlearning.poseidon.api.annotation.dao.hbase.HbaseField;
import com.voxlearning.poseidon.core.util.*;
import com.voxlearning.storage.core.hbase.IHbaseHelper;
import com.voxlearning.poseidon.storage.hbase.util.ConvertsUtil;
import com.voxlearning.poseidon.storage.hbase.util.HBasesUtil;
import com.voxlearning.storage.core.util.HbaseAnnontationUtil;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author hao.su<hao.su@17zuoye.com>
 * @version 2017-06-21
 * @since 17-6-21
 */
public class HbaseHelperImpl implements IHbaseHelper {

    private static Logger logger = LoggerFactory.getLogger(HbaseHelperImpl.class);

    @Override
    public boolean createTable(Class clazz) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        String family = HbaseAnnontationUtil.getFamilyName(clazz);
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableNameStr));
        if (hasTable(clazz)) {
            logger.warn("该表已存在");
            return false;
        }
        table.addFamily(new HColumnDescriptor(family));
        HBasesUtil.getAdmin().createTable(table);
        logger.info(tableNameStr + "已创建");
        return true;
    }


    @Override
    public boolean deleteTable(Class clazz) throws IOException {
        Admin admin = HBasesUtil.getAdmin();
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableNameStr));
        if (hasTable(clazz)) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
            logger.info(tableNameStr + "删除成功");
            return true;
        } else {
            logger.warn(tableNameStr + "表不存在");
            return false;
        }
    }

    @Override
    public boolean hasTable(Class clazz) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Admin admin = HBasesUtil.getAdmin();
        TableName tableName = TableName.valueOf(tableNameStr);
        return admin.tableExists(tableName);
    }

    @Override
    public boolean put(Object t) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(t.getClass());
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        Object keyValue = HbaseAnnontationUtil.getRowKeyValue(t);
        Put put = new Put(ConvertsUtil.convertToBytes(keyValue));
        table.put(AddCell(put, t));
        logger.info(t + "已添加");
        return true;
    }

    @Override
    public <T> boolean putBatch(Class<T> clazz, List<T> list) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        List<Row> batch = new LinkedList<>();
        for (T t : list) {
            Object keyValue = HbaseAnnontationUtil.getRowKeyValue(t);
            Put put = new Put(ConvertsUtil.convertToBytes(keyValue));
            put = AddCell(put, t);
            batch.add(put);
        }
        Object[] results = new Object[list.size()];//用于存放批量操作结果
        try {
            table.batch(batch, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public <T> T get(Object rowkey, Class<T> clazz) throws IOException {
        List<String> columns = AnnoatationsUtil.getFieldsBySpecAnnoatationWithString(clazz, HbaseField.class);
        return get(rowkey, clazz, columns);
    }

    @Override
    public <T> T get(Object rowkey, Class<T> clazz, List<String> columns) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        String family = HbaseAnnontationUtil.getFamilyName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        Get get = new Get(ConvertsUtil.convertToBytes(rowkey));
        for (String column : columns) {
            get.addColumn(family.getBytes(), column.getBytes());
        }
        Result result = table.get(get);
        if (result.isEmpty()) return null;
        return ResultToModel(result, clazz);
    }

    @Override
    public <T> List scan(Object startrowkey, Object stoprowkey, Class<T> clazz) throws IOException {
        return scan(startrowkey, stoprowkey, clazz);
    }

    @Override
    public <T> List<T> scan(Object startrowkey, Object stoprowkey, Class<T> clazz, List<String> columns) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        String family = HbaseAnnontationUtil.getFamilyName(clazz);
        Scan scan = new Scan();
        for (String column : columns) {
            scan.addColumn(family.getBytes(), column.getBytes());
        }
        ResultScanner scanner = table.getScanner(scan);
        List<T> list = new LinkedList<>();
        for (Result result : scanner) {
            T model = ResultToModel(result, clazz);
            list.add(model);
        }
        return list;
    }

    @Override
    public <T> boolean delete(Object rowkey, Class<T> clazz) throws IOException {
        if (!hasRow(rowkey, clazz)) {
            return false;
        }
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        Delete delete = new Delete(ConvertsUtil.convertToBytes(rowkey));
        table.delete(delete);
        return true;
    }

    @Override
    public <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        List<Row> batch = new LinkedList<>();
        for (T t : list) {
            Object keyValue = HbaseAnnontationUtil.getRowKeyValue(t);
            if (!hasRow(keyValue, clazz)) {
                continue;
            }
            Delete delete = new Delete(ConvertsUtil.convertToBytes(keyValue));
            batch.add(delete);
        }
        Object[] results = new Object[list.size()];
        try {
            table.batch(batch, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean hasRow(Object rowkey, Class clazz) throws IOException {
        String tableNameStr = HbaseAnnontationUtil.getTableName(clazz);
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf(tableNameStr));
        Get get = new Get(ConvertsUtil.convertToBytes(rowkey));
        Result result = table.get(get);
        return !result.isEmpty();
    }

    public static void modifySchema() throws IOException {

        Admin admin = HBasesUtil.getAdmin();
        TableName tableName = TableName.valueOf("TABLE_NAME");
        if (admin.tableExists(tableName)) {
            logger.warn("Table does not exist.");
            System.exit(-1);
        }

        HTableDescriptor table = new HTableDescriptor(tableName);

        // Update existing table
        HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
        newColumn.setCompactionCompressionType(Compression.Algorithm.GZ);
        newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
        admin.addColumn(tableName, newColumn);

        // Update existing column family
        HColumnDescriptor existingColumn = new HColumnDescriptor("CF_DEFAULT");
        existingColumn.setCompactionCompressionType(Compression.Algorithm.GZ);
        existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
        table.modifyFamily(existingColumn);
        admin.modifyTable(tableName, table);

        // Disable an existing table
        admin.disableTable(tableName);

        // Delete an existing column family
        admin.deleteColumn(tableName, "CF_DEFAULT".getBytes("UTF-8"));

        // Delete a table (Need to be disabled first)
        admin.deleteTable(tableName);

    }

    /**
     * 解析model并向put对象添加字段值
     *
     * @Param put put
     * @Param dataModel
     */
    private static Put AddCell(Put put, Object model) {
        String family = HbaseAnnontationUtil.getFamilyName(model.getClass());
        /**field字段为基本类型和集合类型的处理*/
        List<Field> fields = AnnoatationsUtil.getFieldsBySpecAnnoatation(model.getClass(), HbaseField.class);
        for (Field filed : fields) {
            Object value = JackBeanUtil.getProperty(model, filed.getName());
            if (value == null) continue;
            String type = filed.getGenericType().toString();
            if (type.startsWith("java.util.List")) {
                StringBuilder newValue = new StringBuilder();
                List list = (List) value;
                for (Object o : list) {
                    String oo = CommonUtil.filterSpecChar(o.toString());
                    newValue.append(oo).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (list.size() == 0) {
                    newValue = new StringBuilder();
                } else {
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            if (type.startsWith("java.util.Set")) {
                StringBuilder newValue = new StringBuilder();
                Set set = (Set) value;
                for (Object o : set) {
                    String oo = CommonUtil.filterSpecChar(o.toString());
                    newValue.append(oo).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (set.size() == 0) {
                    newValue = new StringBuilder();
                } else {
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            if (type.startsWith("java.util.Map")) {
                StringBuilder newValue = new StringBuilder();
                Map map = (Map) value;
                for (Object key : map.keySet()) {
                    String keyStr = CommonUtil.filterSpecChar(key.toString());
                    String valueStr = CommonUtil.filterSpecChar(map.get(key).toString());
                    newValue.append(keyStr).append(Constant.MAP_KV_CH).append(valueStr).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (map.size() == 0) {
                    newValue = new StringBuilder();
                } else {
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            put.addColumn(family.getBytes(), filed.getName().getBytes(), ConvertsUtil.convertToBytes(value));
        }
        return put;
    }

    private static <T> T ResultToModel(Result result, Class<T> clazz) {
        String family = HbaseAnnontationUtil.getFamilyName(clazz);
        T model = JackBeanUtil.getInstanceByClass(clazz);
        List<Field> fields = AnnoatationsUtil.getFieldsBySpecAnnoatation(model.getClass(), HbaseField.class);
        for (Field field : fields) {
            String fieldName = field.getName();
            byte[] b = result.getValue(family.getBytes(), fieldName.getBytes());
            Object value;
            Type type = field.getGenericType();
            if (type.toString().startsWith("java.util.List")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value == null) continue;
                value = Arrays.asList(value.toString().split(Constant.COLLECTION_SPLIT_CH));
            } else if (type.toString().startsWith("java.util.Set")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value == null) continue;
                Set set = new HashSet();
                set.addAll(Arrays.asList(value.toString().split(Constant.COLLECTION_SPLIT_CH)));
                value = set;
            } else if (type.toString().startsWith("java.util.Map")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value == null) continue;
                Map map = new HashMap();
                for (Object o : value.toString().split(Constant.COLLECTION_SPLIT_CH)) {
                    Object[] arr = o.toString().split(Constant.MAP_KV_CH);
                    map.put(arr[0], arr[1]);
                }
                value = map;
            } else {
                value = ConvertsUtil.bytesToValue(type, b);
            }
            JackBeanUtil.setProperty(model, fieldName, value);
        }
        return model;
    }
}
