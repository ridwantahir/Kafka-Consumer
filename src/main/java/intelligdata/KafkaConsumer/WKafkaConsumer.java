package intelligdata.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

import intelligdata.KafkaProducer.WebActivity;

public class WKafkaConsumer implements Runnable{
	static Logger logger=Logger.getLogger(WKafkaConsumer.class);
	int numberOfThreads =5;
	AtomicBoolean closed=new AtomicBoolean(false);
	KafkaConsumer<Integer, WebActivity> consumer;
	List<String> topics;
	ExecutorService executorService;
	ScheduledExecutorService executor;
	ScheduledProcessor scheledProcessor;
	Map<Integer, Long> summaryMap;
	Map<Integer, List<ConsumerRecord<Integer, WebActivity>>> activityCache;
	public WKafkaConsumer(String consumerID,String groupID,String brokers, String topic) {
		summaryMap=new ConcurrentHashMap<>();
		activityCache=new ConcurrentHashMap<>();
		topics=Arrays.asList(topic.split(","));
		consumer=new KafkaConsumer<Integer, WebActivity>(getConsumerConfig(consumerID, groupID, brokers));
		executorService=Executors.newFixedThreadPool(numberOfThreads);
		executor = Executors.newScheduledThreadPool(1);
		startScheduler();
	}
	public void run(){
		consumer.subscribe(topics);
		while(!closed.get()){
			Set<TopicPartition>assignment=consumer.assignment();
			while(scheledProcessor.IsBusy().get()){
				try {				
					consumer.pause(assignment);
					Thread.sleep(100);
					consumer.resume(assignment);
					consumer.poll(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ConsumerRecords<Integer, WebActivity> records=consumer.poll(100);
			Set<TopicPartition> partitions=records.partitions();
			for(TopicPartition partition: partitions){
				Integer partitionId=partition.partition();
				List<ConsumerRecord<Integer, WebActivity>> partitionRecords=records.records(partition);
				List<ConsumerRecord<Integer, WebActivity>>curActivities=activityCache.getOrDefault(partitionId, new ArrayList<ConsumerRecord<Integer, WebActivity>>());
				curActivities.addAll(partitionRecords);
				activityCache.put(partitionId, curActivities);
			}
		}
		shutDown();
		
	}
	void closeConsumer(){
		try{
			 closed.set(true);
		     consumer.wakeup();
		}catch(Exception e){
			
		}
		finally{
			consumer.close();
		}
	}
	public void shutDown(){
		closeConsumer();
		executorService.shutdown();
		try {
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(!executorService.isTerminated()){
				logger.warn("Failed to shutdown gracefully... forcing exit");
			}
			executorService.shutdownNow();
			logger.info("Executor Terminated");
		}
	}
	public Properties getConsumerConfig(String consumerID, String groupID, String brokers){
		Properties props=new Properties();
		props.put("bootstrap.servers", brokers);
	    props.put("group.id", groupID);
	    props.put("auto.offset.reset", "earliest");
	    props.put(" session.timeout.ms", "1000");
	    props.put("max.poll.interval.ms", "1000");
	    props.put("max.poll.records", "1000");
	    props.put("enable.auto.commit", "false");
	    props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
	    props.put("value.deserializer", "intelligdata.serde.WebActivityDeserializer");
	    return props;
	}
	public void startScheduler(){
		scheledProcessor=new ScheduledProcessor(activityCache, executorService, summaryMap);
		int initialDelay = 0;
		int period = 5;
		Runnable scheledProcessor=new ScheduledProcessor(activityCache, executorService, summaryMap);
		executor.scheduleAtFixedRate(scheledProcessor, initialDelay, period, TimeUnit.SECONDS);
	}
}
