package cn.com.warlock.scheduler;

import java.util.Date;

import cn.com.warlock.scheduler.model.JobConfig;

/**
 * 任务运行日志持久化接口
 */
public interface JobLogPersistHandler {

	public void onSucess(JobConfig conf, Date nextFireTime);
	
	public void onError(JobConfig conf, Date nextFireTime,Exception e);
}
