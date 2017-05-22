package intelligdata.serde;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class JavaDeserializer<T extends Serializable> {

	public T deserialie(byte[] bytearray) throws IOException, ClassNotFoundException{
		ByteArrayInputStream stream=new ByteArrayInputStream(bytearray);
		ObjectInputStream in=new ObjectInputStream(stream);
		return (T)(in.readObject());
		
	}
}
