package com.voxlearning.poseidon.redis.cluster.model;


import java.util.List;
import java.util.Objects;


public class RedisClusterConfig {

    public static final String SEPARATOR = ":";

    private String name;
    private List<String> hosts;
    private int timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisClusterConfig that = (RedisClusterConfig) o;
        return timeout == that.timeout &&
                Objects.equals(name, that.name) &&
                Objects.equals(hosts, that.hosts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, hosts, timeout);
    }
}
