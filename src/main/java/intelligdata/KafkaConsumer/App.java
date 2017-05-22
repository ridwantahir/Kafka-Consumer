package intelligdata.KafkaConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	WKafkaConsumer consumer=new WKafkaConsumer("1", "g1", "localhost:9092", "user-activities");
    	executor.submit(consumer);
    }
}
