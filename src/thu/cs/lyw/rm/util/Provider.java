package thu.cs.lyw.rm.util;

import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider {
	private boolean isInit;
	@Expose private ProviderType providerType;
	@Expose @SerializedName("properties") private HashMap<String, String> simpleProperties;
	private HashMap<String, Object> advancedProperties;
	public Provider (){
		simpleProperties = new HashMap<String, String>();
		advancedProperties = new HashMap<String, Object>();
		isInit = false;
	}
	public Provider (ProviderType providerType){
		this.providerType = providerType;
		simpleProperties = new HashMap<String, String>();
		advancedProperties = new HashMap<String, Object>();
		isInit = false;
	}
	public void addProperty(String key, Object value){
		if (value.getClass().equals(String.class)){
			simpleProperties.put(key, (String)value);
		} else {
			advancedProperties.put(key, value);
		}
	}
	public void delProperty(String key){
		simpleProperties.remove(key);
		advancedProperties.remove(key);
	}
	public Object getProperty(String key){
		Object obj = simpleProperties.get(key);
		if (obj != null) return obj;
		else return advancedProperties.get(key);
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
