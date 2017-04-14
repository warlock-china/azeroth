package cn.com.warlock.mybatis.core;

import org.apache.ibatis.plugin.Invocation;

import cn.com.warlock.mybatis.plugin.MybatisPluginContext;

/**
 * mybatis插件拦截处理器接口
 */
public interface InterceptorHandler {

    void start(MybatisPluginContext context);

    void close();

    Object onInterceptor(Invocation invocation) throws Throwable;

    void onFinished(Invocation invocation, Object result);

    InterceptorType getInterceptorType();
}
