package MatrixMul;

import java.util.*;
import mapreduce.MapReduce;


public class Main {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int matrix_size = 128;
		double[][] matrixA;
		double[][] matrixB;
		int parallelNum = 1;
		
		if(args.length > 0){
			matrix_size = new Integer(args[0]).intValue();
		}
		if(args.length > 1){
			parallelNum = new Integer(args[1]).intValue();
		}
		
		System.out.println("matrix size = " + String.valueOf(matrix_size));
		matrixA = new double[matrix_size][matrix_size];
		matrixB = new double[matrix_size][matrix_size];
		
		init(matrixA);
		try{
			Thread.sleep(1); 
			}catch(InterruptedException e){
				System.out.println("óêêîí≤êÆÇÃÇΩÇﬂÇÃsleep");
			}
		init(matrixB);
		
		/*
		 * BÇì]íuÇ≥ÇπÇÈÅB
		 */
		
		//rotate(matrixB);
		
		/*
		 * MapReduce
		 */
	//	show(matrixA);
	//	show(matrixB);
		
		MapReduce<Integer, double[], Integer, Double, Integer, Double> mmMR = new MapReduce<Integer, double[], Integer, Double, Integer, Double>(MapMM.class, ReduceMM.class, "MAP_REDUCE");
		mmMR.setResultOutput(false);
		mmMR.setParallelThreadNum(parallelNum);
		
		
		for(int i = 0; i < matrix_size; i++){
			for(int j = 0; j < matrix_size; j++){
				for(int k = 0; k < matrix_size; k++){
					double[] ijV = {matrixA[i][k], matrixB[k][j]};
					mmMR.addKeyValue(i*matrix_size+j, ijV);
				}
			}
		}
		
		System.out.println("Making matrix is finished.");
		
		long start = System.nanoTime();

		mmMR.run();
		
		long stop = System.nanoTime();
		
		System.out.println("MatrixMul time is " + String.valueOf((double)(stop - start) / 1000000000));

	}
	
	
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
