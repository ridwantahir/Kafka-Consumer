package intelligdata.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import intelligdata.KafkaProducer.WebActivity;


public class WebActivitySerializer implements Serializer<WebActivity>{

	JavaSerializer<WebActivity> jSerializer;
	public WebActivitySerializer(){
		jSerializer=new JavaSerializer<WebActivity>();
	}
	public void close() {	
	}
	public void configure(Map<String, ?> arg0, boolean arg1) {	
	}

	public byte[] serialize(String arg0, WebActivity arg1) {
		try {
			return jSerializer.serialize(arg1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
