package thu.cs.lyw.rm.evaluation;

import thu.cs.lyw.rm.manager.RTask;
import thu.cs.lyw.rm.util.NodeType;
import thu.cs.lyw.rm.util.ProviderType;
import thu.cs.lyw.rm.util.SystemImage;

public class REvaluation {
	public ProviderType provider;
	public RTask task;
	public SystemImage image;
	public NodeType type;
	public double cpu;
	public double memory;
	public double disk;
	public REvaluation(RTask task){
		this.task = task;
		provider = task.getProviderType();
		type = task.getPerferredType();
		image = task.getImage();
		cpu = task.cpu;
		memory = task.memory;
		disk = task.disk;
	}
}
