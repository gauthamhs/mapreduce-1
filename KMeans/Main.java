package KMeans;

import java.util.Date;
import java.util.List;
import java.util.Random;
import mapreduce.MapReduce;


/*
 * Kmeans�����s����N���X
 *�@�񎟌��̍��W��ɓ_�݂���v���b�g�𕡐��̃O���[�v�ł܂Ƃ߂�
 */
public class Main {

	/**
	 * KMeans�����s����B
	 * @param args �������ɂ��ׂĂ̍��W�̐��A�������ɃO���[�v�̐��A��O�����ɕ��񐔂��i�[����
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int vectornum = 100;
		int clusternum = 3;
		int parallelnum = 1;
		
		VectorKM[] vectors;
		VectorKM[] clusters;
		VectorKM[] tmpclusters;
		
		if(args.length > 0){
			vectornum = new Integer(args[0]).intValue();
		}
		if(args.length > 1){
			clusternum = new Integer(args[1]).intValue();
		}
		if(args.length > 2){
			parallelnum = new Integer(args[2]).intValue();
		}
		
		vectors= new VectorKM[vectornum];
		clusters= new VectorKM[clusternum];
		tmpclusters= new VectorKM[clusternum];

		
		initializeVectorKM(vectors, 0, 0, 1, 1);
		initializeVectorKM(clusters, 0.4f, 0.4f, 0.6f, 0.6f);
		
//		showVectorKM(vectors);
//		showVectorKM(clusters);

		MapReduce<VectorKM, VectorKM[], VectorKM, VectorKM, VectorKM, VectorKM> mmKM = new MapReduce<VectorKM, VectorKM[], VectorKM, VectorKM, VectorKM, VectorKM>(MapKM.class, ReduceKM.class, "MAP_REDUCE");
		mmKM.setResultOutput(true);
		mmKM.setParallelThreadNum(parallelnum);
		
		kernelKMeans(mmKM, vectors, clusters, tmpclusters);
		
		System.out.println("");
		
		while(!compareClusters(clusters, tmpclusters)){
			for(int i = 0; i < tmpclusters.length; i++)
				clusters[i] = tmpclusters[i];
			mmKM = new MapReduce<VectorKM, VectorKM[], VectorKM, VectorKM, VectorKM, VectorKM>(MapKM.class, ReduceKM.class, "MAP_REDUCE");
			mmKM.setResultOutput(true);
			mmKM.setParallelThreadNum(parallelnum);
			kernelKMeans(mmKM, vectors, clusters, tmpclusters);
		
			System.out.println("");
		}
		
		for(int i = 0; i < tmpclusters.length; i++)
			clusters[i] = tmpclusters[i];
		
		mmKM = new MapReduce<VectorKM, VectorKM[], VectorKM, VectorKM, VectorKM, VectorKM>(MapKM.class, ReduceKM.class, "MAP_SHUFFLE");

		for(VectorKM vkm : vectors){
			mmKM.addKeyValue(vkm, clusters);
		}
		
		mmKM.run();
		
		
	}
	
	/*
	 * Kmeans�̌v�Z���s�����\�b�h
	 * 1.MapReduce�N���X��mmKM�̃L�[�Ƀv���b�g�A�o�����[�ɑS�N���X�^�̏d�_�̍��W���i�[����Ă���z������ꂼ������
	 * 2.mmKM��MapReduce�������s�킹��
	 * 3.makeNewCluster�ŐV�����N���X�^���l������
	 * @param mmKM MapReduce�̏������s���N���X
	 * @param vectors �v���b�g�̍��W����ꂽ�z��
	 * @param clusters�@�N���X�^�̏d�_�̍��W���������z��
	 * @param tmpclusters 
	 * @param parallelnum ����
	 */
	public static void kernelKMeans(MapReduce<VectorKM, VectorKM[], VectorKM, VectorKM, VectorKM, VectorKM> mmKM,
			VectorKM[] vectors,
			VectorKM[] clusters,
			VectorKM[] tmpclusters
			){
		
		for(VectorKM vkm : vectors){
			mmKM.addKeyValue(vkm, clusters);
		}

		mmKM.run();
		
		makeNewCluster(clusters, tmpclusters, mmKM.getKeys(), mmKM.getValues());
	}
		
	/*	
	 * �񎟌����W�̃v���b�g�������_���ɐ������邽�߂̃��\�b�h
	 * @param vectors �񎟌����W�̔z��B���̒��Ƀv���b�g�����������
	 * @param xmin �񎟌����W�ɂ�����x���̍ŏ��l
	 * @param ymin �񎟌����W�ɂ�����y���̍ŏ��l
	 * @param xmax �񎟌����W�ɂ�����x���̍ő�l
	 * @param ymin �񎟌����W�ɂ�����y���̍ő�l
	 */
	public static void initializeVectorKM(
			VectorKM[] vectors,
			float xmin,
			float ymin,
			float xmax,
			float ymax){
		Date d = new Date();
		Random rdm = new Random(d.getTime());
		
		for(int i = 0; i < vectors.length; i++){
			vectors[i] = new VectorKM(xmin + rdm.nextFloat()*(xmax - xmin), ymin + rdm.nextFloat()*(ymax - ymin));
		}
	}
	
	/*
	 * �v���b�g�̍��W��Console�ɕ\�����邽�߂̃��\�b�h
	 * @param vectors �v���b�g���i�[����Ă���z��
	 */
	public static void showVectorKM(VectorKM[] vectors){
		for(VectorKM vkm : vectors){
			System.out.println("(x, y) = (" + vkm.getX() + ", " + vkm.getY() + ")");
		}
	}
	
	/*
	 * �v�Z��̃N���X�^�̏d�_���v�Z���邽�߂̃��\�b�h 
	 * 
	 * @param cluster �v�Z�ɗp�����N���X�^�̏d�_���i�[����Ă���z��
	 * @param cluster �v�Z��̃N���X�^�̏d�_���i�[�����z��
	 * @param keylist �v�Z��̃L�[���i�[����Ă��郊�X�g
	 * @param valuelist �v�Z��̃o�����[���i�[����Ă��郊�X�g
	 * keylist��valuelist�̓C���f�b�N�X�ő��݂ɑΉ����Ă���
	 */
	public static void makeNewCluster(
			VectorKM[] clusters,
			VectorKM[] tmpclusters,
			List<VectorKM> keylist,
			List<VectorKM> valuelist){
		int clusterindex;
		
		for(int i = 0; i < clusters.length; i++){
			if((clusterindex = keylist.indexOf(clusters[i])) != -1)
				tmpclusters[i] = valuelist.get(clusterindex);
			else
				tmpclusters[i] = clusters[i];
		}
	}
	
	/*
	 * �N���X�^�̍��W���r�����g�������Ȃ�ΐ^��Ԃ����\�b�h
	 * @param tmpcluster �v�Z��̃N���X�^�̏d�_���i�[����Ă���z��
	 * @param cluster �v�Z��̃N���X�^�̏d�_���i�[����Ă���z��
	 */
	public static boolean compareClusters(
			VectorKM[] clusters,
			VectorKM[] tmpclusters
			){
		int fixclusternum = 0;
		for(int i = 0; i < tmpclusters.length; i++){
			for(int j = i; j < clusters.length; j++){
				if(tmpclusters[i].equals(clusters[j])){
					fixclusternum++;
				}
			}
		}
		return (fixclusternum == clusters.length) ? true : false;
	}

}
