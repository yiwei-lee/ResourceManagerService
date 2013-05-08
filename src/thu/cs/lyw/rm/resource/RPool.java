package thu.cs.lyw.rm.resource;

import java.util.HashMap;
import thu.cs.lyw.rm.adapter.RAdapter;
import thu.cs.lyw.rm.adapter.RAdapterFactory;
import thu.cs.lyw.rm.evaluation.REvaluation;
import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.ProviderType;

public class RPool {
	HashMap<ProviderType, RAdapter> adapterMap;
	public RPool(){
		adapterMap = new HashMap<ProviderType, RAdapter>();
	}
	public void initProvider(Provider provider){
		RAdapter adapter = getAdapter(provider.getType());
		adapter.initProvider(provider);
	}
	public RNode getNode(Provider provider, REvaluation evaluation){
		RAdapter adapter = getAdapter(provider.getType());
		RNode node = null;
		try {
			node = adapter.getNodeFromProvider(provider, evaluation);
		} catch (Exception e) {
			System.err.println("Failed to get node from provider : ");
			e.printStackTrace();
		}
		return node;
	}
	public void releaseNode(RNode node){
		RAdapter adapter = getAdapter(node.getProvider().getType());
		adapter.releaseNodeFromProvider(node);
	}
	private RAdapter getAdapter(ProviderType providerType){
		RAdapter adapter = adapterMap.get(providerType);
		if (adapter == null){
			adapter = RAdapterFactory.getAdapterInstance(providerType);
			adapterMap.put(providerType, adapter);
		}
		return adapter;
	}
}
