package com.voxlearning.poseidon.storage.hbase;

import com.google.common.collect.Lists;

import com.voxlearning.poseidon.storage.hbase.client.impl.HbaseHelperImpl;
import com.voxlearning.storage.core.hbase.IHbaseHelper;

import java.io.IOException;
import java.util.*;


public class TestAccessor {

    public static void testHbase() throws IOException {
        IHbaseHelper accessor = new HbaseHelperImpl();
        accessor.deleteTable(UserModel.class);
        accessor.createTable(UserModel.class);
        System.out.println(accessor.hasTable(UserModel.class));
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
        UserModel ins = accessor.get(1010001L, UserModel.class);
        System.out.println(ins);
        System.out.println(accessor.hasRow(1010001L, UserModel.class));
        //删除指定记录
        //System.out.println(accessor.delete(1010001L,UserModel.class));
        //批量删除
        //System.out.println(accessor.deleteBatch(list...,UserModel.class));
        //扫描记录
        //System.out.println(accessor.scan(....));
    }

    public static void main(String[] args) throws IOException {
        testHbase();
    }

}
