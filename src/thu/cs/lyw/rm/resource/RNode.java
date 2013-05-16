package thu.cs.lyw.rm.resource;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

import thu.cs.lyw.rm.util.Provider;

public class RNode {
	@Expose private String IP;
	@Expose private String status;
	@Expose private Provider provider;
	@Expose private HashMap<String, String> properties;
	public RNode(Provider provider){
		this.provider = provider;
		status = "unknown";
		IP = "unknown";
		properties = new HashMap<String, String>();
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
	public void setProvider(Provider provider){
		this.provider = provider;
	}
	public void addProperty(String key, String value){
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
