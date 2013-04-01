package MatrixMul;

import mapreduce.*;

public class MapMM extends Mapper<Integer, double[], Integer, Double>{
	
	/**
	 * �s��v�Z��Map�������s�����\�b�h
	 * �L�[�̍��W�����Ƃ߂�̂ɕK�v�ȗv�f�̐ς����Ƃ߂�
	 * (non-Javadoc)
	 * @see mapreduce.Mapper#map()
	 */
	protected void map(){
		emit(this.getInputKey(), this.getInputValue()[0]*this.getInputValue()[1]);
	}
}
