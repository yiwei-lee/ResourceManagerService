package thu.cs.lyw.rm.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import thu.cs.lyw.rm.evaluation.REvaluation;
import thu.cs.lyw.rm.evaluation.REvaluator;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.resource.RPool;
import thu.cs.lyw.rm.util.NodeType;
import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.ProviderType;
import thu.cs.lyw.rm.util.Strategy;

@SuppressWarnings("unused")
public class RManager {
	private RPool resourcePool;
	private int userId;
	private HashMap<ProviderType, ArrayList<Provider>> providerMap;
	private PriorityQueue<RTask> taskQueue;
	private HashMap<RTask, RNode> taskMap;
	private REvaluator evaluator;
	private Strategy strategy;
	
	public RManager(int id){
		userId = id;
		resourcePool = new RPool();
		providerMap = new HashMap<ProviderType, ArrayList<Provider>>();
		taskQueue = new PriorityQueue<RTask>();
		taskMap = new HashMap<RTask, RNode>();
		evaluator = new REvaluator();
	}
	public void addProvider(Provider provider){
		//Provider should be added before using the system to manage resources.
		resourcePool.initProvider(provider);
		ArrayList<Provider> providers = providerMap.get(provider.getType());
		if (providers == null){
			providers = new ArrayList<Provider>();
			providerMap.put(provider.getType(), providers);
		}
		providers.add(provider);
	}
	public RNode getNode(RTask task) {
		REvaluation evaluation = evaluator.evaluate(task);
		ArrayList<Provider> providers = providerMap.get(evaluation.provider);
		if (providers == null){
			System.err.println("Error : no " + evaluation.provider.toString() + " provider.");
			return null;
		}
		Provider provider = providers.get(0);
		RNode node = resourcePool.getNode(provider, evaluation);
		return node;
	}
	public void releaseNode(RNode node) {
		resourcePool.releaseNode(node);
	}
}
