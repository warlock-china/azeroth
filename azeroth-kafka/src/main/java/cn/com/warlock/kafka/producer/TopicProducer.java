package cn.com.warlock.kafka.producer;

import cn.com.warlock.kafka.message.DefaultMessage;
import cn.com.warlock.kafka.producer.handler.ProducerEventHandler;

public interface TopicProducer {
	
	void addEventHandler(ProducerEventHandler eventHandler);
	/**
	 * 发送消息（可选择发送模式）
	 * @param topic
	 * @param message
	 * @param asynSend 是否异步发送
	 * @return
	 */
	boolean publish(final String topic, final DefaultMessage message,boolean asynSend);
	
	void close();
}
