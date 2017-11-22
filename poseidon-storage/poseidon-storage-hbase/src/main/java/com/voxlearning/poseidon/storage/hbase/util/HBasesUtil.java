package com.voxlearning.poseidon.storage.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;


public class HBasesUtil {
    private static Configuration config = null;
    private static Connection connection = null;

    static {
        config = HBaseConfiguration.create();
        config.addResource(new Path("hbase-site.xml"));
    }

    public static Connection getConnection(){
        if(connection == null || connection.isClosed()){
            try {
                connection = ConnectionFactory.createConnection(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static Admin getAdmin(){
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
