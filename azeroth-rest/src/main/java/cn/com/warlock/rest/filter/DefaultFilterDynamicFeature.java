package cn.com.warlock.rest.filter;

import java.lang.reflect.Method;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import cn.com.warlock.rest.filter.annotation.ResponseFormat;
import cn.com.warlock.rest.filter.annotation.ResponseFormat.FormatType;

public class DefaultFilterDynamicFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		// 获取资源方法
		Method resourceMethod = resourceInfo.getResourceMethod();

		if (resourceMethod != null) {

			// 获取FormatJson注解
			ResponseFormat formatJson = resourceMethod.getAnnotation(ResponseFormat.class);

			if(formatJson == null || formatJson.type().equals(FormatType.JSON)){
				context.register(DefaultWebFilter.class);
			}

		}
	}
}
