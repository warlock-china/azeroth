/**
 * 
 */
package cn.com.warlock.cache.redis;

import org.springframework.beans.factory.DisposableBean;

public interface JedisProvider<S,B> extends DisposableBean{

	public S get();
	
	public B getBinary();
	
	public void release();
	
	public String mode();
	
	public String groupName();

}
