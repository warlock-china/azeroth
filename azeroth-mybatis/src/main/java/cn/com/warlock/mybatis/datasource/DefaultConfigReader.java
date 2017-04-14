package cn.com.warlock.mybatis.datasource;

import cn.com.warlock.common.util.ResourceUtils;

public class DefaultConfigReader implements ConfigReader {

    @Override
    public String get(String key) {
        return ResourceUtils.get(key);
    }

    @Override
    public String getIfAbent(String key, Object defaulttVal) {
        if (defaulttVal == null)
            return get(key);
        return ResourceUtils.get(key, defaulttVal.toString());
    }

    @Override
    public boolean containKey(String key) {
        return get(key) != null;
    }

}
