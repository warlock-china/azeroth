package cn.com.warlock.kafka.producer.handler;

import java.io.Closeable;

import org.apache.kafka.clients.producer.RecordMetadata;

import cn.com.warlock.kafka.message.DefaultMessage;

public interface ProducerEventHandler extends Closeable{

	public void onSuccessed(String topicName, RecordMetadata metadata);

	public void onError(String topicName, DefaultMessage message,boolean isAsynSend);

}
