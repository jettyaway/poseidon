package com.voxlearning.poseidon.redis.cluster.builder;

import redis.clients.jedis.JedisCluster;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-22
 * @since 17-12-22
 */
public interface IJedisBuilder {


    void init(String settingName);

    JedisCluster getCluster(String groupName);
}
