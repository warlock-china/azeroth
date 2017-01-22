package cn.com.warlock.test.sch;

import org.apache.commons.lang3.RandomUtils;

import cn.com.warlock.scheduler.AbstractJob;
import cn.com.warlock.scheduler.JobContext;

public class DemoTask extends AbstractJob{

	int count = 1;
	@Override
	public void doJob(JobContext context) throws Exception {
		System.out.println("\n=============\nDemoTask1=====>"+count+"\n===============\n");
		Thread.sleep(RandomUtils.nextLong(1000, 2000));
		count++;
	}

	@Override
	public boolean parallelEnabled() {
		return false;
	}

}
