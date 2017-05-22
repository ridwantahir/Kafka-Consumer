package intelligdata.KafkaProducer;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class WKafkaProducer extends Thread{
	KafkaProducer<Integer, WebActivity> producer;
	private AtomicBoolean closed=new AtomicBoolean(false);
	String topic;
	public WKafkaProducer(String brokers, String topic) {
		producer= new KafkaProducer<Integer, WebActivity>(getProducerConfig(brokers));
		this.topic=topic;
	}
	@Override
	public void run(){
		try{
			while(!closed.get()){
				WebActivity activity = WebActivityGenerator.getWebActivity();
				ProducerRecord<Integer, WebActivity> record= new ProducerRecord<Integer, WebActivity>(topic, new Integer(activity.userID),activity);
				producer.send(record, new SendListener<Integer, WebActivity>(record));
				Thread.sleep(100);
			}			
	}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			producer.close();
		}
	}
	 private  Properties getProducerConfig(String brokers) {
		    Properties config = new Properties();
		    config.put("bootstrap.servers", brokers);
		    config.put("acks", "all");
		    config.put("retries", 0);
		    config.put("batch.size", 16384);
		    config.put("linger.ms", 1);
		    config.put("buffer.memory", 33554432);
		    config.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		    config.put("value.serializer", "intelligdata.serde.WebActivitySerializer");
		    config.put("partitioner.class","intelligdata.KafkaProducer.WebActivityPartitioner");
		    return config;
		  }
	 public void shutdown() {
         closed.set(true);
     }
}
