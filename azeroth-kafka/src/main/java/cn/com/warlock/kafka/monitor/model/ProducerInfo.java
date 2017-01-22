package cn.com.warlock.kafka.monitor.model;

import java.util.ArrayList;
import java.util.List;

public class ProducerInfo {

	private String name;
	
	private List<ProducerTopicInfo> producerTopics;
	
	public ProducerInfo() {}

	public ProducerInfo(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProducerTopicInfo> getProducerTopics() {
		return producerTopics == null ? (producerTopics = new ArrayList<>()) : producerTopics;
	}

	public void setProducerTopics(List<ProducerTopicInfo> producerTopics) {
		this.producerTopics = producerTopics;
	}
	
	
}
