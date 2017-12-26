package com.voxlearning.poseidon.redis.cluster.builder;


import com.voxlearning.poseidon.core.concurrent.ReentrantLocker;
import com.voxlearning.poseidon.core.util.StrUtil;
import com.voxlearning.poseidon.redis.cluster.IJedisConnection;
import com.voxlearning.poseidon.redis.cluster.JedisClusterConnectionImpl;
import com.voxlearning.poseidon.redis.cluster.model.RedisClusterConfig;
import com.voxlearning.poseidon.settings.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 读取配置文件,生成链接并缓存
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-22
 * @since 17-12-22
 */
public class JedisClusterBuilderImpl extends ReentrantLocker implements IJedisBuilder {

    private Logger logger = LoggerFactory.getLogger(JedisClusterBuilderImpl.class);

    private static JedisClusterBuilderImpl INSTANCE = null;

    private static final Map<String, JedisCluster> buffer = new HashMap<>();
    private static final Map<String, RedisClusterConfig> holders = new HashMap<>();

    private static IJedisConnection jedisConnection = new JedisClusterConnectionImpl();

    /***
     * 默认配置文件名称
     */
    private String settingName = "redis.setting";

    /**
     * 构造函数
     *
     * @param settingName 配置文件名称
     */
    private JedisClusterBuilderImpl(String settingName) {
        if (StrUtil.isNotBlank(settingName)) {
            this.settingName = settingName;
        }
        init(settingName);
    }

    public static JedisClusterBuilderImpl instance(String settingName) {
        if (Objects.isNull(INSTANCE)) {
            synchronized (JedisClusterBuilderImpl.class) {
                if (Objects.isNull(INSTANCE)) {
                    if (StrUtil.isBlank(settingName)) {
                        INSTANCE = new JedisClusterBuilderImpl(settingName);
                    }
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void init(String settingName) {
        settingName = StrUtil.defaultString(settingName, this.settingName);
        Setting setting = new Setting(settingName);
        List<String> groups = setting.getGroups();
        if (Objects.isNull(groups)) {
            return;
        }
        for (String group : groups) {
            holders.put(group, (RedisClusterConfig) setting.toBean(group, new RedisClusterConfig()));
        }
        if (holders.isEmpty()) {
            logger.warn("your import cache redis module but not hava default redis setting file.");
            return;
        }
        holders.keySet().forEach(this::getCluster);
    }

    @Override
    public JedisCluster getCluster(String groupName) {
        return doInLock(() -> {
            if (buffer.containsKey(groupName)) {
                return buffer.get(groupName);
            }
            RedisClusterConfig config = holders.get(groupName);
            JedisCluster cluster = jedisConnection.getCluster(config);
            buffer.put(groupName, cluster);
            return cluster;
        });
    }
}
