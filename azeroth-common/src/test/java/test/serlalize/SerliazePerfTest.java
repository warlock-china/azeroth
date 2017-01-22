package test.serlalize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.warlock.common.json.JsonUtils;
import cn.com.warlock.common.serializer.JavaSerializer;
import cn.com.warlock.common.serializer.SerializeUtils;
import test.User;

public class SerliazePerfTest {

	public static void main(String[] args) throws IOException {
		JavaSerializer javaSerializer = new JavaSerializer();
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			users.add(new User(i+1000, "user"+i));
		}
		
		byte[] bytes = javaSerializer.serialize(users);
		System.out.println(bytes.length);
		System.out.println(SerializeUtils.serialize(users).length);
		System.out.println(JsonUtils.toJson(users).getBytes().length);
		
		
	}
}
