package intelligdata.serde;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class JavaSerializer<T  extends Serializable> {

	public byte[] serialize(T t) throws IOException{
		ByteArrayOutputStream outarray=new ByteArrayOutputStream();
		ObjectOutputStream out=new ObjectOutputStream(outarray);
		out.writeObject(t);
		out.close();
		return outarray.toByteArray();
	}
	
}
