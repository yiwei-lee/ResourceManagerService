package thu.cs.lyw.user;

import java.util.HashMap;
import thu.cs.lyw.rm.adapter.RAdapter;
import thu.cs.lyw.rm.resource.RPool;
import thu.cs.lyw.rm.util.ProviderType;

@SuppressWarnings("unused")
public class RUser {
	private RPool resourcePool;
	private HashMap<ProviderType, HashMap<Integer, RAdapter>> adapterMap;
	private int userID;
	static int nextID = 0;
	public RUser(){
		this.userID = nextID++;
		resourcePool = new RPool();
		adapterMap = new HashMap<ProviderType, HashMap<Integer, RAdapter>>();
	}
}
