package KMeans;

import mapreduce.Mapper;

/**
 * KMeans��Mapper�̃T�u�N���X
 */
public class MapKM  extends Mapper<VectorKM, VectorKM[], VectorKM, VectorKM>{

	double measuredDistance(VectorKM v1, VectorKM v2){
		return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
	}
	
	/**
	 * Kmeans��map�������s�����\�b�h
	 * 1.���̓o�����[�Ƃ��ė^����ꂽ�N���X�^�̏d�_�̒��ŁA�ł����̓L�[�Ƃ��ė^����ꂽ�v���b�g�̍��W�ɋ߂����̂�T��
	 * 2.�L�[���ł��߂��N���X�^�̏d�_�̍��W�A�o�����[����̓L�[�̃v���b�g�̍��W�Ƃ���emit�֐��ɓn��
	 * @see mapreduce.Mapper#map()
	 */
	@Override
	protected void map() {
		// TODO Auto-generated method stub
		double mindistance = 0;
		VectorKM minvector = new VectorKM();
		
		for(VectorKM vkm : this.getInputValue()){
			double distance = measuredDistance(this.getInputKey(), vkm);
			if(mindistance == 0){
				minvector = vkm;
				mindistance = distance;
			}
			else if(distance < mindistance){
				minvector = vkm;
				mindistance = distance;
			}
		}
		emit(minvector, this.getInputKey());
	}

}
