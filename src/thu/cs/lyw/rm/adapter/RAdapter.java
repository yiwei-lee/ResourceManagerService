package thu.cs.lyw.rm.adapter;

import thu.cs.lyw.rm.evaluation.REvaluation;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.util.Provider;

public abstract class RAdapter {
	public abstract void initProvider(Provider provider);
	public abstract RNode getNodeFromProvider(Provider provider, REvaluation evaluation) throws Exception;
	public abstract void releaseNodeFromProvider(RNode node);
//	public abstract String getPerformance(RNode node);
}
