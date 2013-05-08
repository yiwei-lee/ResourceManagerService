package thu.cs.lyw.rm.evaluation;

import java.util.Iterator;
import thu.cs.lyw.rm.manager.RTask;
import thu.cs.lyw.rm.util.ProviderType;
import thu.cs.lyw.rm.util.Strategy;

public class REvaluator {
	private Strategy strategy;
	public REvaluator(Strategy strategy){
		this.strategy = strategy;
	}
	public REvaluator(){
		this.strategy = Strategy.FIFO;
	}
	//Get evaluation;
	public REvaluation evaluate(RTask task){
		REvaluation evaluation = new REvaluation(task);
		if (evaluation.provider == null){
			//TODO : what to use?
//			Iterator<ProviderType> providers = providerMap.keySet().iterator();
		}
		return evaluation;
	}
	//Sets and gets;
	public Strategy getStrategy(){
		return strategy;
	}
	public void setStrategy(Strategy strategy){
		this.strategy = strategy;
	}
}
