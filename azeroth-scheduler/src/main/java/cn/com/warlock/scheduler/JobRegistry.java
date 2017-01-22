package cn.com.warlock.scheduler;

import java.util.Date;
import java.util.List;

import cn.com.warlock.scheduler.model.JobConfig;

public interface JobRegistry {

	void register(JobConfig conf);
	
	void updateJobConfig(JobConfig conf);
	
	void setRuning(String jobName,Date fireTime);
	
	void setStoping(String jobName,Date nextFireTime,Exception e);
	
	JobConfig getConf(String jobName,boolean forceRemote);
	
	void unregister(String jobName);
	
	List<JobConfig> getAllJobs();
}
