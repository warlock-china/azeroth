package cn.com.warlock.mybatis.core;

import org.apache.ibatis.plugin.Invocation;

import cn.com.warlock.mybatis.plugin.MybatisInterceptor;

/**
 * mybatis插件拦截处理器接口
 */
public interface InterceptorHandler {

    void start(MybatisInterceptor context);

    void close();

    Object onInterceptor(Invocation invocation) throws Throwable;

    void onFinished(Invocation invocation, Object result);

    int interceptorOrder();
}
