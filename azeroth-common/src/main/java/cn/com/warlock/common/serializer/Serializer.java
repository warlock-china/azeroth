package cn.com.warlock.common.serializer;

import java.io.IOException;

/**
 * 对象序列化接口
 */
public interface Serializer {
	
	public String name();

	public byte[] serialize(Object obj) throws IOException ;
	
	public Object deserialize(byte[] bytes) throws IOException ;
	
}
