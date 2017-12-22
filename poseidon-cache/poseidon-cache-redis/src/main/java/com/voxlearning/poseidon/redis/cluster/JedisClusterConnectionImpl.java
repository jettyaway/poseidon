package com.voxlearning.poseidon.redis.cluster;

import com.voxlearning.poseidon.redis.cluster.exception.JedisClusterException;
import com.voxlearning.poseidon.redis.cluster.model.RedisClusterConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-20
 * @since 17-12-20
 */
public class JedisClusterConnectionImpl implements IJedisConnection {

    @Override
    public JedisCluster getCluster(RedisClusterConfig redisConfig) {
        List<String> hosts = redisConfig.getHosts();
        String name = redisConfig.getName();
        int timeout = redisConfig.getTimeout();
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        for (String host : hosts) {
            if (!host.contains(RedisClusterConfig.SEPARATOR)) {
                throw new JedisClusterException("Host and port must separator with[:]");
            }
            String[] split = host.split(RedisClusterConfig.SEPARATOR);
            if (split.length < 2) {
                throw new JedisClusterException("Host [%s] is error format", host);
            }
            hostAndPorts.add(new HostAndPort(split[0], Integer.parseInt(split[1])));
        }
        return new JedisCluster(hostAndPorts, timeout);
    }
}
