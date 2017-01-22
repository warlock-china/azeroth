package cn.com.warlock.mybatis.plugin.cache;

/**
 * 定义按主键增删改查的方法名
 */
public interface CacheMethodDefine {

	String selectName();
	
	String insertName();
	
	String updateName();
	
	String deleteName();
	
	String selectAllName();
}
