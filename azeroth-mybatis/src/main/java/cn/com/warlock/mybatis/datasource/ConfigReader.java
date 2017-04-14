package cn.com.warlock.mybatis.datasource;

public interface ConfigReader {

    public String get(String key);

    public String getIfAbent(String key, Object defaulttVal);

    boolean containKey(String key);
}
