package cn.com.warlock.cache.local;

import java.io.Closeable;

public interface Level1CacheProvider extends Closeable{

	void start();
	boolean set(String cacheName,String key,Object value);
	
    <T> T get(String cacheName,String key);
    
    void remove(String cacheName,String key);
    
    void remove(String cacheName);
    
    void clearAll();
    
}
