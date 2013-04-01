package mapreduce;

import java.util.concurrent.Callable;

/**
 * 
 * @author takafumi
 *
 * Reducer�N���X�����Ɏ��s���邽�߂�Callable�����������N���X
 * 
 * @param <IntermediateKey> Reduce�����̓��͂̃L�[�N���X
 * @param <IntermediateValue> Reduce�����̓��͂̃o�����[�N���X
 * @param <OutputReduceKey> Reduce�����̏o�͂̃L�[�N���X
 * @param <OutputReduceValue> Reduce�����̏o�͂̃o�����[�N���X
 */
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