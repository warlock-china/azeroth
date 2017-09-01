package cn.com.warlock.mybatis.plugin.rwseparate;

import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.warlock.mybatis.core.InterceptorHandler;
import cn.com.warlock.mybatis.datasource.DataSourceContextHolder;
import cn.com.warlock.mybatis.plugin.MybatisInterceptor;

/**
 * 读写分离自动路由处理
 */
public class RwRouteHandler implements InterceptorHandler {

    protected static final Logger logger = LoggerFactory.getLogger(RwRouteHandler.class);

    public static final String NAME = "rwRoute";

    @Override
    public Object onInterceptor(Invocation invocation) throws Throwable {

        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        //已指定强制使用
        if (DataSourceContextHolder.get().isForceUseMaster()) {
            logger.debug("Method[{}] force use Master..", ms.getId());
            return null;
        }

        //读方法
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
            if (!ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                DataSourceContextHolder.get().useSlave(true);
                logger.debug("Method[{} use Slave Strategy..", ms.getId());
            }
        } else {
            logger.debug("Method[{}] use Master Strategy..", ms.getId());
            DataSourceContextHolder.get().useSlave(false);
        }

        return null;
    }

    @Override
    public void onFinished(Invocation invocation, Object result) {
    }

    @Override
    public void start(MybatisInterceptor context) {
    }

    @Override
    public void close() {
    }

    @Override
    public int interceptorOrder() {
        return 1;
    }
}
