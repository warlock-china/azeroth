package cn.com.warlock.cache.redis.cluster;

import java.util.HashSet;
import java.util.Set;

import cn.com.warlock.cache.redis.JedisProvider;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 集群 redis服务提供者
 */
public class JedisClusterProvider implements JedisProvider<JedisCluster, BinaryJedisCluster> {

    public static final String MODE            = "cluster";

    private Integer            maxRedirections = 3;        //重试3次

    private JedisCluster       jedisCluster;
    private BinaryJedisCluster binaryJedisCluster;

    private String             groupName;

    /**
     * 
     */
    public JedisClusterProvider(String groupName, JedisPoolConfig jedisPoolConfig, String[] servers,
                                int timeout) {
        this.groupName = groupName;
        Set<HostAndPort> nodes = this.parseHostAndPort(servers);
        jedisCluster = new JedisCluster(nodes, timeout, maxRedirections, jedisPoolConfig);
        binaryJedisCluster = new BinaryJedisCluster(nodes, timeout, maxRedirections,
            jedisPoolConfig);
    }

    private Set<HostAndPort> parseHostAndPort(String[] servers) {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();

            for (String part : servers) {
                String[] ipAndPort = part.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public JedisCluster get() {
        return jedisCluster;
    }

    @Override
    public BinaryJedisCluster getBinary() {
        return binaryJedisCluster;
    }

    @Override
    public void release() {
    }

    @Override
    public void destroy() throws Exception {
        jedisCluster.close();
    }

    @Override
    public String mode() {
        return MODE;
    }

    @Override
    public String groupName() {
        return groupName;
    }

}