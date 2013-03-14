package mapreduce;

import java.util.concurrent.Callable;

public class ReduceCallable<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue> implements Callable<Reducer<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue>>{
	Reducer<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue> reducer;
	
	ReduceCallable(){
	}
	
	ReduceCallable(Reducer<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue> reducer){
		this.reducer = reducer;
	}
	
	public Reducer<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue> call(){
		this.reducer.reduce();
		return this.reducer;
	}
}