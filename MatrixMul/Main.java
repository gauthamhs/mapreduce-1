package MatrixMul;

import java.util.*;
import mapreduce.MapReduce;


public class Main {

	/**
	 * @param args �������ɍs��̒����A�������ɕ��񐔂����ꂼ��i�[����z��
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int matrixSize = 128;
		double[][] matrixA;
		double[][] matrixB;
		int parallelNum = 1;
		
		if(args.length > 0){
			matrixSize = new Integer(args[0]).intValue();
		}
		if(args.length > 1){
			parallelNum = new Integer(args[1]).intValue();
		}
		
		System.out.println("matrix size = " + String.valueOf(matrixSize));
		matrixA = new double[matrixSize][matrixSize];
		matrixB = new double[matrixSize][matrixSize];
		
		init(matrixA);
		try{
			Thread.sleep(1); 
			}catch(InterruptedException e){
				System.out.println("���������̂��߂�sleep");
			}
		init(matrixB);
		
	//	show(matrixA);
	//	show(matrixB);
		
		MapReduce<Integer, double[], Integer, Double, Integer, Double> mmMR = new MapReduce<Integer, double[], Integer, Double, Integer, Double>(MapMM.class, ReduceMM.class, "MAP_REDUCE");
		mmMR.setResultOutput(false);
		mmMR.setParallelThreadNum(parallelNum);
		
		
		for(int i = 0; i < matrixSize; i++){
			for(int j = 0; j < matrixSize; j++){
				for(int k = 0; k < matrixSize; k++){
					double[] ijV = {matrixA[i][k], matrixB[k][j]};
					mmMR.addKeyValue(i*matrixSize+j, ijV);
				}
			}
		}
		
		System.out.println("Making matrix is finished.");
		
		long start = System.nanoTime();

		mmMR.run();
		
		long stop = System.nanoTime();
		
		System.out.println("MatrixMul time is " + String.valueOf((double)(stop - start) / 1000000000));

	}
	
	
	/**
	 * �s��Ƀ����_���Ȕz����i�[�����������郁�\�b�h
	 * @param matrix �s��
	 */
	public static void init(double[][] matrix){
		Date d = new Date();
		Random rdm = new Random(d.getTime());
//		System.out.println(d.getTime());
		
		for(int i = 0; i < matrix.length; i ++){
			for(int j = 0; j < matrix.length; j++){
				matrix[i][j] = rdm.nextDouble();
			}
		}
	}
	
	/**
	 * �s�����]���邽�߂̃��\�b�h
	 * @param matrix �s��
	 */
	public static void rotate(double[][] matrix){
		double tmp;
		for(int i = 0; i < matrix.length; i ++){
			for(int j = i+1; j < matrix.length; j++){
				tmp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = tmp;
			}
		}
	}
	
	/**
	 * �s���\�����郁�\�b�h
	 * @param matrix �s��
	 */
	public static void show(double[][] matrix){
		for(int i = 0; i < matrix.length; i ++){
			for(int j = 0; j < matrix.length; j++){
				System.out.print(matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

}
