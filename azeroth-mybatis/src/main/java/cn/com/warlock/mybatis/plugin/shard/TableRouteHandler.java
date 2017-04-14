package cn.com.warlock.mybatis.plugin.shard;

import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import cn.com.warlock.mybatis.core.InterceptorHandler;
import cn.com.warlock.mybatis.core.InterceptorType;
import cn.com.warlock.mybatis.plugin.MybatisPluginContext;

/**
 * 分库自动路由处理
 */
public class TableRouteHandler implements InterceptorHandler, InitializingBean {

    protected static final Logger logger = LoggerFactory.getLogger(TableRouteHandler.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public Object onInterceptor(Invocation invocation) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onFinished(Invocation invocation, Object result) {
        // TODO Auto-generated method stub

    }

    @Override
    public InterceptorType getInterceptorType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start(MybatisPluginContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }
}
