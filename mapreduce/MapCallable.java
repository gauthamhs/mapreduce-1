package mapreduce;

import java.util.concurrent.Callable;

public class MapCallable<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> implements Callable<Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue>>{
	Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> mapper;
	
	MapCallable(){
	}
	
	MapCallable(Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> mapper){
		this.mapper = mapper;
	}
	
	public Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> call(){
		this.mapper.map();
		return this.mapper;
	}
}
