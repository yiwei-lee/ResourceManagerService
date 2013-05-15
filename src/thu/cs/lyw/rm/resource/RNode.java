package thu.cs.lyw.rm.resource;

import java.util.HashMap;
import thu.cs.lyw.rm.util.Provider;

public class RNode {
	private String IP;
	private String status;
	private HashMap<String, Object> properties;
	private Provider provider;
	public RNode(Provider provider){
		this.provider = provider;
		status = "unknown";
		IP = "unknown";
		properties = new HashMap<String, Object>();
	}
	public void setIP(String IP){
		this.IP = IP;
	}
	public String getIP(){
		return IP;
	}
	public Provider getProvider(){
		return provider;
	}
	public void addProperty(String key, Object value){
		properties.put(key, value);
	}
	public Object getProperty(String key){
		return properties.get(key);
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
