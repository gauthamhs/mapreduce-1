package MatrixMul;

import java.util.*;

import mapreduce.MapReduce;
import WordCount.MapWC;
import WordCount.ReduceWC;

public class Main {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int matrix_size = 4;
		double[][] matrixA;
		double[][] matrixB;
		double[][] matrixC;
		

		
		if(args.length > 1){
			matrix_size = new Integer(args[1]).intValue();
		}
		matrixA = new double[matrix_size][matrix_size];
		matrixB = new double[matrix_size][matrix_size];
		matrixC = new double[matrix_size][matrix_size];
		
		init(matrixA);
		init(matrixB);
		
		/*
		 * Bを転置させる。
		 */
		
		//rotate(matrixB);
		
		/*
		 * MapReduce
		 */
		
		show(matrixA);
		show(matrixB);
		
		MapReduce<Integer, double[], Integer, Double, Integer, Double> mmMR = new MapReduce(MapMM.class, ReduceMM.class, "MAP_REDUCE");
		
		/*
		 * ijV
		 * matrixCの(i, j)を求める為に必要な値
		 */
		
		//double[] ijV = new double[2];
		for(int i = 0; i < matrix_size; i++){
			for(int j = 0; j < matrix_size; j++){
				for(int k = 0; k < matrix_size; k++){
					double[] ijV = {matrixA[i][k], matrixB[k][j]};
					System.out.println(String.valueOf(ijV[0]) + " "+ String.valueOf(ijV[0]));
					mmMR.addKeyValue(i*matrix_size+j, ijV);
				}
			}
		}
		
		mmMR.run();
	}
	
	public static void init(double[][] matrix){
		Random rdm = new Random(new Date().getTime());
		
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
