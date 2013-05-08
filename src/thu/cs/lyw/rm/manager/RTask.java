package thu.cs.lyw.rm.manager;

import thu.cs.lyw.rm.util.NodeType;
import thu.cs.lyw.rm.util.ProviderType;
import thu.cs.lyw.rm.util.SystemImage;

public class RTask {
	private NodeType perferredType;
	private ProviderType providerType;
	private SystemImage image;
	public String securityGroup;
	public String keyName;
	public double cpu;
	public double memory;
	public double disk;
	
	public RTask(ProviderType providerType, String system, String perferredType, String keyName, String securityGroup){
		this.providerType = providerType;
		this.perferredType = NodeType.valueOf(perferredType);
		image = new SystemImage(system);
		this.securityGroup = securityGroup;
		this.keyName = keyName;
		cpu = 0.0;
		memory = 0.0;
		disk = 0.0;
	}
	public RTask(ProviderType providerType, String system, String perferredType){
		this.providerType = providerType;
		this.perferredType = NodeType.valueOf(perferredType);
		image = new SystemImage(system);
		securityGroup = "default";
		keyName = "rm-test";
		cpu = 0.0;
		memory = 0.0;
		disk = 0.0;
	}
	public RTask(ProviderType providerType, String system){
		this.providerType = providerType;
		this.perferredType = NodeType.MICRO;
		image = new SystemImage(system);
		cpu = 0.0;
		memory = 0.0;
		disk = 0.0;
	}
	
	public RTask(String system, String perferredType){
		this.providerType = null;
		this.perferredType = NodeType.valueOf(perferredType);
		image = new SystemImage(system);
		securityGroup = "default";
		keyName = "rm-test";
		cpu = 0.0;
		memory = 0.0;
		disk = 0.0;
	}
	public RTask(String system){
		this.providerType = null;
		this.perferredType = NodeType.MICRO;
		image = new SystemImage(system);
		cpu = 0.0;
		memory = 0.0;
		disk = 0.0;
	}
	
	public NodeType getPerferredType(){
		return perferredType;
	}
	public SystemImage getImage(){
		return image;
	}
	public ProviderType getProviderType(){
		return providerType;
	}
	public void setResource(int index, double value){
		switch(index){
		case 0:
			cpu = value;
			break;
		case 1:
			memory = value;
			break;
		case 2:
			disk = value;
			break;
		default:
			break;
		}
	}
}

class RTaskFailure {
	
}

class RTaskFeedback {
	
}