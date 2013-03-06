package WordCount;

import mapreduce.*;

public class ReduceWC extends Reducer<String, Integer, String, Integer>{

	
	protected void reduce(){
		int sum = 0;
		for(Integer num : this.ivalues){
			sum++;
		}
		emit(this.ikey, sum);
	}
}
