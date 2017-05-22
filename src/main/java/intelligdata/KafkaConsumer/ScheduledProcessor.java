package intelligdata.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import intelligdata.KafkaProducer.WebActivity;

public class ScheduledProcessor implements Runnable{
	Map<Integer, List<ConsumerRecord<Integer, WebActivity>>> activityCache;
	ExecutorService executorService;
	Map<Integer, Long> summaryMap;
	AtomicBoolean isBusy=new AtomicBoolean(false);
	public ScheduledProcessor(Map<Integer, List<ConsumerRecord<Integer, WebActivity>>> activityCache,ExecutorService executorService,
			Map<Integer, Long> summaryMap) {
		this.activityCache=activityCache;
		this.executorService=executorService;
		this.summaryMap=summaryMap;
	}	
	public void run() {
		isBusy.set(true);
		List<Future<Boolean>> status=new ArrayList<>();
		Set<Integer> partitions=activityCache.keySet();
		for(Integer key:partitions){
			List<ConsumerRecord<Integer, WebActivity>> activities=activityCache.get(key);
			Callable<Boolean> processor=new RecordProcessor(activities,summaryMap);
			Future<Boolean> finsihed=executorService.submit(processor);	
			status.add(finsihed);
		}
		while(status.stream().filter(s->!(s.isDone())).count()!=0);
		activityCache.clear();
		System.out.println(summaryMap);
		summaryMap.clear();
		isBusy.set(false);
		
	}
	public AtomicBoolean IsBusy() {
		return isBusy;
	}
	
	
}
