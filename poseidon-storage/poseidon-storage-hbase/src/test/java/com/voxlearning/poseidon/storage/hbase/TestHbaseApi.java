package com.voxlearning.poseidon.storage.hbase;

import com.google.common.collect.Lists;
import com.voxlearning.poseidon.storage.hbase.client.impl.HbaseHelperImpl;
import com.voxlearning.poseidon.storage.hbase.util.HBasesUtil;
import com.voxlearning.storage.core.hbase.IHbaseHelper;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * UnitTest
 *
 * @author:hao.su<hao.su@17zuoye.com>
 * @version:2017-06-21
 * @since:17-6-21
 */
public class TestHbaseApi {
    private Logger logger = LoggerFactory.getLogger(TestHbaseApi.class);

    IHbaseHelper accessor;

    @Before
    public void before() {
        accessor = new HbaseHelperImpl();
    }

    @Test
    public void deleteTable() throws IOException {
        accessor.deleteTable(UserModel.class);
    }

    @Test
    public void createTable() throws IOException {
        accessor.createTable(UserModel.class);
    }

    @Test
    public void addUser() throws IOException {
        UserModel user = new UserModel();
        user.setUserId(1010001L);
        user.setUserName("hao.su");
        user.setAge(30);
        Map<String, Integer> scoresMap = new HashMap();
        scoresMap.put("history", 95);
        scoresMap.put("geography", 98);
        scoresMap.put("math", 100);
        user.setScoresMap(scoresMap);
        List<String> hobiesList = new ArrayList<>();
        hobiesList.add("basketball");
        hobiesList.add("swimming");
        hobiesList.add("shoot");
        user.setHobiesList(hobiesList);
        Set<Integer> yearsSet = new HashSet<>();
        yearsSet.add(10);
        yearsSet.add(25);
        yearsSet.add(65);
        user.setYears(yearsSet);
        user.setEnd(true);
        Pepole pepole = new Pepole();
        pepole.setAge(100);
        pepole.setName("hao.su");
        List<Pepole> pepoles = Lists.newArrayList();
        pepoles.add(pepole);
        pepole = new Pepole();
        pepole.setAge(99);
        pepole.setName("hao.su1");
        pepoles.add(pepole);
        user.setPepoles(pepoles);
        user.setSchooling(0.2);
        accessor.put(user);
    }

    @Test
    public void queryUserTest() throws IOException {
        UserModel ins = accessor.get(1010001L, UserModel.class);
        logger.info("{}", ins);
    }

    @Test
    public void existsTest() throws IOException {
        logger.info("{}", accessor.hasRow(1010001L, UserModel.class));
    }

    @Test
    public void testHomeWork() throws IOException {
        logger.info("*****{}", accessor.get("20160912-ENGLISH-201609_57d6aeb6e92b1b57f27ad253-333880100", HomeWork.class));
    }

    @Test
    public void testOriginApi() throws IOException {
        Table table = HBasesUtil.getConnection().getTable(TableName.valueOf("my-table-user"));
        Get get = new Get(Bytes.toBytes(1010001L));
        //get.addColumn("cf".getBytes(), "subGrasp".getBytes());

        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println("family:" + Bytes.toString(kv.getFamily()));
            System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.out.println("value:" + Bytes.toString(kv.getValue()));
            System.out.println("Timestamp:" + kv.getTimestamp());
            System.out.println("-------------------------------------------");
        }
    }

}
