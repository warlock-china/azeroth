package cn.com.warlock.rest.resolver;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import cn.com.warlock.common.json.JsonMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson ObjectMapper 解析器
 * 
 */
@Provider
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

	static JsonMapper jsonMapper = JsonMapper.nonNullMapper().dateAndTimestampConvert(true);

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return jsonMapper.getMapper();
	}
}