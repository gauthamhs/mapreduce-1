package MatrixMul;

import mapreduce.*;

public class ReduceMM extends Reducer< Integer, Double, Integer, Double>{

	/**
	 * �s��v�Z��Map�������s�����\�b�h
	 * �L�[�̍��W�����߂�̂ɕK�v�ȗv�f�̘a�����߂�
	 * (non-Javadoc)
	 * @see mapreduce.Reducer#reduce()
	 */
	protected void reduce(){
		double sum = 0;
		for(double d : this.getInputValue()){
			sum += d;
		}
		emit(this.getInputKey(), new Double(sum));
	}
}
