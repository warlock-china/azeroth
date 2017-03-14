package cn.com.warlock.kafka.handler;
/**
 * ClassName:OffsetLogHanlder <br/>
 * Function: 消息消费偏移量记录器. <br/>
 * Reason:   TODO ADD REASON. <br/>
 * Date:     Mar 14, 2017 4:56:47 PM <br/>
 * @author   warlock
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface OffsetLogHanlder {

	/**
	 * 获取上一次记录的已经处理的偏移量
	 * @param group
	 * @param topic
	 * @param partition
	 * @return
	 */
	long getLatestProcessedOffsets(String group,String topic,int partition);
	
	/**
	 * 处理前记录偏移量
	 * @param group
	 * @param topic
	 * @param partition
	 * @param offset
	 */
	void saveOffsetsBeforeProcessed(String group,String topic,int partition,long offset);
	
	/**
	 * 处理后记录偏移量
	 * @param group
	 * @param topic
	 * @param partition
	 * @param offset
	 */
	void saveOffsetsAfterProcessed(String group,String topic,int partition,long offset);
}


