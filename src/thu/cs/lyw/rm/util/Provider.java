package thu.cs.lyw.rm.util;

import java.util.HashMap;

public class Provider {
	private boolean isInit;
	private ProviderType providerType;
	private HashMap<String, Object> properties;
	
	public Provider (ProviderType providerType){
		this.providerType = providerType;
		properties = new HashMap<String, Object>();
		isInit = false;
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
