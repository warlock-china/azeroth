package cn.com.warlock.rest;

import cn.com.warlock.rest.excetion.ExcetionWrapper;

public interface CustomConfig {

	/**
	 * 对异常包装返回给前端
	 * @return
	 */
	ExcetionWrapper createExcetionWrapper();
	
	/**
	 * 返回要扫描的包（rest接口）
	 * @return
	 */
	String packages();
}
