package cn.com.warlock.mybatis.crud;

import java.util.List;

import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import cn.com.warlock.mybatis.crud.builder.DeleteBuilder;
import cn.com.warlock.mybatis.crud.builder.GetByPrimaryKeyBuilder;
import cn.com.warlock.mybatis.crud.builder.InsertBuilder;
import cn.com.warlock.mybatis.crud.builder.UpdateBuilder;
import cn.com.warlock.mybatis.parser.EntityInfo;
import cn.com.warlock.mybatis.parser.MybatisMapperParser;
import cn.com.warlock.mybatis.plugin.cache.name.DefaultCacheMethodDefine;

public class GeneralSqlGenerator {

    public static DefaultCacheMethodDefine methodDefines = new DefaultCacheMethodDefine();

    private LanguageDriver languageDriver;
    private Configuration  configuration;

    public GeneralSqlGenerator(Configuration configuration) {
        this.configuration = configuration;
        this.languageDriver = configuration.getDefaultScriptingLanuageInstance();
    }

    public void generate() {
        if (languageDriver == null) { languageDriver = configuration.getDefaultScriptingLanuageInstance(); }
        List<EntityInfo> entityInfos = MybatisMapperParser.getEntityInfos();
        for (EntityInfo entity : entityInfos) {
            GetByPrimaryKeyBuilder.build(configuration, languageDriver, entity);
            InsertBuilder.build(configuration, languageDriver, entity);
            UpdateBuilder.build(configuration, languageDriver, entity);
            DeleteBuilder.build(configuration, languageDriver, entity);
        }
    }
}
