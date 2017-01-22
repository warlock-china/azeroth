/**
 * 
 */
package cn.com.warlock.cache.redis.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.warlock.cache.redis.JedisProvider;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 标准（单服务器）redis服务提供者
 */
public class JedisStandaloneProvider implements JedisProvider<Jedis,BinaryJedis>{
	
	protected static final Logger logger = LoggerFactory.getLogger(JedisStandaloneProvider.class);

	
	public static final String MODE = "standalone";

	private ThreadLocal<Jedis> context = new ThreadLocal<>();
	
	private JedisPool jedisPool;
	
	private String groupName;
	

	public JedisStandaloneProvider(String groupName,JedisPoolConfig jedisPoolConfig, String[] servers, int timeout, String password, int database, String clientName) {
		super();
		this.groupName = groupName;
		String[] addrs = servers[0].split(":");
		jedisPool = new JedisPool(jedisPoolConfig, addrs[0], Integer.parseInt(addrs[1].trim()), timeout, password, database, clientName);
	}

	public Jedis get() throws JedisException {
        Jedis jedis = context.get();
        if(jedis != null)return jedis;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            if(jedis!=null){
            	jedis.close();
            }
            throw e;
        }
        context.set(jedis);
        if(logger.isTraceEnabled()){
        	logger.trace(">>get a redis conn[{}],Host:{}",jedis.toString(),jedis.getClient().getHost());
        }
        return jedis;
    }
 
	@Override
	public BinaryJedis getBinary() {	
		return get();
	}
	
	public void release() {
		Jedis jedis = context.get();
        if (jedis != null) {
        	context.remove();
        	jedis.close();
        	if(logger.isTraceEnabled()){
            	logger.trace("<<release a redis conn[{}]",jedis.toString());
            }
        }
    }

	
	@Override
	public void destroy() throws Exception{
		jedisPool.destroy();
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
