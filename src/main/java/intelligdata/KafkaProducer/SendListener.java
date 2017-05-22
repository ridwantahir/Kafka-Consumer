package intelligdata.KafkaProducer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

public class SendListener<K,V> implements Callback{
	Logger logger=Logger.getLogger(SendListener.class);
	ProducerRecord<K, V> record;
	public SendListener(ProducerRecord<K, V> record){
		this.record=record;
	}	
	public void onCompletion(RecordMetadata recordmeta, Exception e) {
		if(e==null){
			//String message="Sent key: "+ this.record.key()+" sent at: "+this.record.timestamp();
			//logger.info(message);
		}
		else{
			String message="Failed to Send key: "+ this.record.key()+" sent at: "+this.record.timestamp();
			logger.warn(message, e);
		}
		
	}

}
