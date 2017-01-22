package cn.com.warlock.mybatis.plugin.cache;

/**
 * 缓存过期时间
 */
public class CacheExpires {
	
	public final static long IN_1MIN = 60;
	
	public final static long IN_3MINS = 60 * 3; 
	
	public final static long IN_5MINS = 60 * 5;

	public final static long IN_1HOUR = 60 * 60;
	
	public final static long IN_1DAY = IN_1HOUR * 24;
	
	public final static long IN_1WEEK = IN_1DAY * 7;
	
	public final static long IN_1MONTH = IN_1DAY * 30;
	
}
