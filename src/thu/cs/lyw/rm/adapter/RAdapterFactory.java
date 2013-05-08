package thu.cs.lyw.rm.adapter;

import thu.cs.lyw.rm.util.ProviderType;

public class RAdapterFactory {
	public static RAdapter getAdapterInstance(ProviderType providerType){
		RAdapter adapter = null;
		try {
			adapter = (RAdapter)Class.forName("thu.cs.lyw.rm.adapter."+providerType.toString()+"Adapter").
					getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adapter;
	}
}
