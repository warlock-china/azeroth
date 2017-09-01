package cn.com.warlock.spring.helper;

import org.springframework.core.env.Environment;

import cn.com.warlock.spring.InstanceFactory;

public class EnvironmentHelper {

    private static Environment environment;

    public static String getProperty(String key) {
        if (environment == null) {
            synchronized (EnvironmentHelper.class) {
                environment = InstanceFactory.getInstance(Environment.class);
            }
        }
        return environment.getProperty(key);
    }
}
