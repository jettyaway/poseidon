package com.voxlearning.poseidon.redis.cluster;

import com.voxlearning.poseidon.redis.cluster.builder.JedisClusterBuilderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisCluster;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-25
 * @since 17-12-25
 */
public class TestConnection {

    private JedisClusterBuilderImpl jedisClusterBuilder;

    @Before
    public void init() {
        jedisClusterBuilder = JedisClusterBuilderImpl.instance(null);
    }

    @Test
    public void testConnection() {
        JedisCluster cluster = jedisClusterBuilder.getCluster("redis-test");
        Assert.assertNotNull(cluster);
    }
}
