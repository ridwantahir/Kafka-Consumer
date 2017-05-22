package intelligdata.KafkaProducer;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

public class WebActivityPartitioner implements Partitioner{

	public void configure(Map<String, ?> arg0) {
		
	}

	public void close() {		
	}

	public int partition(String topic, Object key, byte[] arg2, Object value, byte[] arg4, Cluster cluster) {
		int numberOfpartitons =cluster.partitionsForTopic(topic).size();
		return key.hashCode()%numberOfpartitons;
	}

}
