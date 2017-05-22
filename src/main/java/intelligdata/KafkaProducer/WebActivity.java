package intelligdata.KafkaProducer;

import java.io.Serializable;

public class WebActivity implements Serializable{

	final String ipAddress;
	final int userID;
	final String pageUrl;
	final String activity;
	final Long timeStamp;
	
	WebActivity(){
		this(null,0,null,null,null);
	}
	public WebActivity(String ipAddress, int userID, String pageUrl, String activity, Long timeStamp) {
		super();
		this.ipAddress = ipAddress;
		this.userID = userID;
		this.pageUrl = pageUrl;
		this.activity = activity;
		this.timeStamp = timeStamp;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public int getUserID() {
		return userID;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public String getActivity() {
		return activity;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	
	
}
