package cn.com.warlock.scheduler;

import cn.com.warlock.scheduler.model.JobConfig;

/**
 * 配置持久化处理器
 */
public interface ConfigPersistHandler {

    /**
     * 启动时合并配置
     * @param groupName
     * @param jobName
     * @return
     */
    void merge(JobConfig config);

    /**
     * 持久化配置
     * @param config
     */
    void persist(JobConfig config);
}
