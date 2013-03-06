package MatrixMul;

import java.util.Map;

import mapreduce.*;

public class ReduceMM extends Reducer< Integer, Double, Integer, Double>{

	protected void reduce(){
		double sum = 0;
		for(double d : this.ivalues){
			sum += d;
		}
		emit(this.ikey, new Double(sum));
	}
}
