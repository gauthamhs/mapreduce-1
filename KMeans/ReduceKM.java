package KMeans;

import mapreduce.Reducer;

/**
 * KMeans��Reducer�̃T�u�N���X
 */
public class ReduceKM extends Reducer<VectorKM, VectorKM, VectorKM, VectorKM>{

	/**
	 * Kmeans��map�������s�����\�b�h
	 * 1.�N���X�^���̍��W����V�����d�_�����߂�
	 * 2.�L�[�����݂̃N���X�^�̏d�_�̍��W�A�o�����[��V�����N���X�^�̏d�_�̍��W�Ƃ���emit�֐��ɂ킽��
	 * (non-Javadoc)
	 * @see mapreduce.Reducer#reduce()
	 */
	@Override
	protected void reduce() {
		// TODO Auto-generated method stub
		float sumX = 0;
		float sumY = 0;
		float valuenum = (float)this.getInputValue().getSize();
		for(VectorKM vkm : this.getInputValue()){
			sumX += vkm.getX();
			sumY += vkm.getY();
		}
		emit(this.getInputKey(), new VectorKM(sumX / valuenum, sumY / valuenum));
	}

}
