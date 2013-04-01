package WordCount;

import mapreduce.*;

public class ReduceWC extends Reducer<String, Integer, String, Integer>{

	
	/** 
	 * �L�[�̒P��̏o���񐔂𑫂����킹��
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
