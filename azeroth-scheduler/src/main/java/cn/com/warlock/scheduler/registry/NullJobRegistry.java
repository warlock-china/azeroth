package cn.com.warlock.scheduler.registry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.warlock.scheduler.JobRegistry;
import cn.com.warlock.scheduler.model.JobConfig;

public class NullJobRegistry implements JobRegistry {

    private Map<String, JobConfig> schedulerConfgs = new ConcurrentHashMap<>();

    @Override
    public void register(JobConfig conf) {
        schedulerConfgs.put(conf.getJobName(), conf);
    }

    @Override
    public void updateJobConfig(JobConfig conf) {
        schedulerConfgs.put(conf.getJobName(), conf);
    }

    @Override
    public void setRuning(String jobName, Date fireTime) {
        JobConfig config = schedulerConfgs.get(jobName);
        config.setRunning(true);
        config.setLastFireTime(fireTime);
    }

    @Override
    public void setStoping(String jobName, Date nextFireTime, Exception e) {
        JobConfig config = schedulerConfgs.get(jobName);
        config.setRunning(false);
        config.setNextFireTime(nextFireTime);
    }

    @Override
    public JobConfig getConf(String jobName, boolean forceRemote) {
        return schedulerConfgs.get(jobName);
    }

    @Override
    public void unregister(String jobName) {
        schedulerConfgs.clear();
        schedulerConfgs = null;
    }

    @Override
    public List<JobConfig> getAllJobs() {
        return new ArrayList<>(schedulerConfgs.values());
    }

}
