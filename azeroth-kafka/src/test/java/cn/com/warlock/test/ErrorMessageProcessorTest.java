package cn.com.warlock.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import cn.com.warlock.kafka.consumer.ErrorMessageDefaultProcessor;
import cn.com.warlock.kafka.message.DefaultMessage;

public class ErrorMessageProcessorTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ErrorMessageDefaultProcessor processor = new ErrorMessageDefaultProcessor();
		
		Demo2MessageHandler messageHandler = new Demo2MessageHandler();
		while(true){			
			processor.submit(new DefaultMessage(RandomStringUtils.random(5, true, true)), messageHandler);
			Thread.sleep(RandomUtils.nextLong(100, 500));
		}
	}

}
