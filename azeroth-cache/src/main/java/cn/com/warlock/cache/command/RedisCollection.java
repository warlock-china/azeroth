package cn.com.warlock.cache.command;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RedisCollection <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:  <br/>
 * date: Jan 17, 2017 8:11:53 PM <br/>
 *
 * @author warlock
 * @version 
 * @since JDK 1.8
 */
public abstract class RedisCollection extends RedisBase {

	protected long expireTime;//过期时间（秒）

	public RedisCollection(String key) {
		this(key,RedisBase.getDefaultExpireSeconds());
	}
	
	/**
	 * 指定组名
	 * @param key
	 * @param groupName
	 */
	public RedisCollection(String key,String groupName) {
		this(key,groupName,RedisBase.getDefaultExpireSeconds());
	}
	
	public RedisCollection(String key,long expireTime) {
		super(key);
		this.expireTime = expireTime;
	}
	
	public RedisCollection(String key,String groupName,long expireTime) {
		super(key,groupName);
		this.expireTime = expireTime;
	}
	
	protected <T> List<T> toObjectList(List<byte[]> datas) {
		List<T> result = new ArrayList<>();
    	if(datas == null)return result;
    	for (byte[] data : datas) {
			result.add((T)valueDerialize(data));
		}
		return result;
	}
	

}
