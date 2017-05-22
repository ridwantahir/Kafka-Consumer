package intelligdata.KafkaProducer;

import java.util.Random;

public class WebActivityGenerator {
	private static Random rand=new Random();
	private static String[]ipAdresses={"34.56.23.12","56.78.234.12","34.65.12.12","54.78.34.12","45.12.34.1"};
	private static int[] userIDs={1,2,3,4,5,6,7,8,9};
	private static String[] pageUrls={"facebook.com","google.com","bbc.com","cnn.com","abc.com","aljazeera.com","xinuanews.com","reuters.com"};
	private static String[] activities={"click","rightclick"};
	
	public static WebActivity getWebActivity(){
		return new WebActivity(ipAdresses[rand.nextInt(ipAdresses.length)],
								rand.nextInt(30),
								pageUrls[rand.nextInt(pageUrls.length)], 
								activities[rand.nextInt(activities.length)],
								System.currentTimeMillis());
	}	
}
