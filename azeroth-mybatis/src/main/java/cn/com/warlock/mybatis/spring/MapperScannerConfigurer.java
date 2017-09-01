package cn.com.warlock.mybatis.spring;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;

import cn.com.warlock.mybatis.crud.GeneralSqlGenerator;

public class MapperScannerConfigurer extends org.mybatis.spring.mapper.MapperScannerConfigurer {

    private ApplicationContext context;
    private String             sqlSessionFactoryName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.context = applicationContext;
    }

    @Override
    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
        super.setSqlSessionFactoryBeanName(sqlSessionFactoryName);
        this.sqlSessionFactoryName = sqlSessionFactoryName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = context.getBean(sqlSessionFactoryName,
                SqlSessionFactory.class);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        //
        new GeneralSqlGenerator(configuration).generate();
    }
}
