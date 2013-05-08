package thu.cs.lyw.rm.util;

import java.util.HashMap;

@SuppressWarnings("unused")
public class Provider {
	private boolean isInit;
	private ProviderType providerType;
	private String username;
	private String password;
	private HashMap<String, Object> properties;
	
	public Provider (ProviderType providerType){
		this.providerType = providerType;
		properties = new HashMap<String, Object>();
		username = null;
		password = null;
		isInit = false;
	}
	public Provider (ProviderType providerType, String username, String password){
		this.providerType = providerType;
		properties = new HashMap<String, Object>();
		this.username = username;
		this.password = password;
	}
	public void addProperty(String key, Object value){
		properties.put(key, value);
	}
	public void delProperty(String key){
		properties.remove(key);
	}
	public Object getProperty(String key){
		return properties.get(key);
	}
	public ProviderType getType(){
		return providerType;
	}
	public void setInit(){
		isInit = true;
	}
	public boolean isInit(){
		return isInit;
	}
}
