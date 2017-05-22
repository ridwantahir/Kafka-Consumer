package intelligdata.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import intelligdata.KafkaProducer.WebActivity;


public class WebActivityDeserializer implements Deserializer<WebActivity>{
	JavaDeserializer<WebActivity> jDSerializer;
	public WebActivityDeserializer(){
		jDSerializer=new JavaDeserializer<WebActivity>();
	}
	public void close() {		
	}

	public void configure(Map<String, ?> arg0, boolean arg1) {
	}

	public WebActivity deserialize(String arg0, byte[] arg1) {
		try {
			return jDSerializer.deserialie(arg1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
