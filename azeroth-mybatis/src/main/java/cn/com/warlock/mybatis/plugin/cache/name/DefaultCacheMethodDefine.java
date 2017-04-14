package cn.com.warlock.mybatis.plugin.cache.name;

import cn.com.warlock.mybatis.plugin.cache.CacheMethodDefine;

public class DefaultCacheMethodDefine implements CacheMethodDefine {

    @Override
    public String selectName() {
        return "getByKey";
    }

    @Override
    public String insertName() {
        return "insert,insertSelective";
    }

    @Override
    public String updateName() {
        return "updateByKey,updateByKeySelective";
    }

    @Override
    public String deleteName() {
        return "deleteByKey";
    }

    @Override
    public String selectAllName() {
        return "selectAll";
    }

}
