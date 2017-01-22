package cn.com.warlock.kafka.producer.handler;

/**
 * 消息已消费回执
 */
public interface ComsumerAckHandler {

	int ack(String msgId);
}
