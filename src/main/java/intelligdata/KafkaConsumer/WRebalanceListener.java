package intelligdata.KafkaConsumer;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

public class WRebalanceListener implements ConsumerRebalanceListener{
	static Logger logger=Logger.getLogger(WRebalanceListener.class);
	public void onPartitionsAssigned(Collection<TopicPartition> arg0) {
		arg0.forEach(topicPartition->{
			System.out.println(topicPartition);
		});		
	}

	public void onPartitionsRevoked(Collection<TopicPartition> arg0) {
		arg0.forEach(topicPartition->{
			System.out.println(topicPartition);
		});
		
	}

}
