package WordCount;

import mapreduce.*;

public class ReduceWC extends Reducer<String, Integer, String, Integer>{

	
	/** 
	 * キーの単語の出現回数を足し合わせる
	 * (non-Javadoc)
	 * @see mapreduce.Reducer#reduce()
	 */
	protected void reduce(){
		int sum = 0;
		for(Integer num : this.getInputValue()){
			sum++;
		}
		emit(this.getInputKey(), sum);
	}
}
