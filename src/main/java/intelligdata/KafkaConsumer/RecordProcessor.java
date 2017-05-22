package intelligdata.KafkaConsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import intelligdata.KafkaProducer.WebActivity;

public class RecordProcessor implements Callable<Boolean>{
	List<ConsumerRecord<Integer,WebActivity>> records;
	Map<Integer, Long> summaryMap;
	public RecordProcessor(List<ConsumerRecord<Integer,WebActivity>> records,Map<Integer, Long> summaryMap) {
		this.records=records;
		this.summaryMap=summaryMap;
	}
	@Override
	public Boolean call() throws Exception {
		final Map<Integer,Long> map=records.stream().collect(Collectors.groupingBy(record->new Integer(record.value().getUserID()), Collectors.counting()));
		map.keySet().stream().forEach(key->{
			summaryMap.put(key, map.get(key));
		});
		return true;
	}

}
