package com.voxlearning.poseidon.redis.cluster;

import com.voxlearning.poseidon.redis.cluster.model.RedisClusterConfig;
import redis.clients.jedis.JedisCluster;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-22
 * @since 17-12-22
 */
public class JedisConnectionFactory {

    private static final JedisConnectionFactory factory = new JedisConnectionFactory();


    private JedisConnectionFactory() {

    }

    public static JedisConnectionFactory getInstance() {
        return factory;
    }

    public static JedisCluster getJedisCluster(RedisClusterConfig config) {
        return null;
    }

}
