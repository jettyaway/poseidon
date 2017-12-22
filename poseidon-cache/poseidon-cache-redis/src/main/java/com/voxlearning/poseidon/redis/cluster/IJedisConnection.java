package com.voxlearning.poseidon.redis.cluster;

import com.voxlearning.poseidon.redis.cluster.model.RedisClusterConfig;
import redis.clients.jedis.JedisCluster;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-20
 * @since 17-12-20
 */
public interface IJedisConnection {
    JedisCluster getCluster(RedisClusterConfig redisConfig);
}
