package cn.com.warlock.mybatis.plugin.cache;

import java.io.Closeable;

/**
 * ClassName: CacheProvider <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:  <br/>
 * date: Jan 18, 2017 10:10:23 AM <br/>
 *
 * @author warlock
 * @version 
 * @since JDK 1.8
 */
public interface CacheProvider extends Closeable{

	<T> T get(String key);
	
	String getStr(String key);
	
	boolean set(String key,Object value,long expired);
	
	boolean remove(String key);
	
	void putGroup(String cacheGroupKey,String key,long expireSeconds);
	
	void removeFromGroup(String cacheGroupKey,String key);
	
	void clearExpiredGroupKeys(String cacheGroup);
	
	void clearGroup(String groupName,boolean containPkCache);

}
