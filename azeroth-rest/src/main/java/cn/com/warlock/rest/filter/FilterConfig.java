package cn.com.warlock.rest.filter;

import cn.com.warlock.common.util.ResourceUtils;

public class FilterConfig {

	public static boolean corsEnabled(){
		return Boolean.parseBoolean(ResourceUtils.get("cors.enabled", "false"));
	}
	
	public static String getCorsAllowOrgin(){
		return ResourceUtils.get("cors.allow.origin", "*");
	}
	
	public static boolean reqRspLogEnabled(){
		return Boolean.parseBoolean(ResourceUtils.get("reqres.log.enabled", "false"));
	}
	
	public static boolean apiDocEnabled(){
		return Boolean.parseBoolean(ResourceUtils.get("apidoc.enabled", "true"));
	}
}
