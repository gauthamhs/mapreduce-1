package WordCount;

import mapreduce.*;
import java.io.*;

public class Main {

	/**
	 * @param args
	 */
	
	/*
	 * WordCount
	 * 引数で与えられたファイル中に出てくる文字の数を数える
	 * 引数でファイルが与えられない場合はこのソースコード中の文字数をカウントする
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * wordcountを行うファイルの指定
		 * ファイルが無い場合はこのソースコードに対しwordcountを行う
		 */
		String filename;
		if(args.length > 1){
			filename = args[2];
		}
		else{
			filename = "";
		}
			
		MapReduce<Integer, String, String, Integer, String, Integer> wcMR = new MapReduce<Integer, String, String, Integer, String, Integer>(MapWC.class, ReduceWC.class, "MAP_REDUCE");
		wcMR.setParallelThreadNum(6);
		
		//初期値をMapReduceに渡す
		try{
			FileReader file = new FileReader(filename);
			BufferedReader buffer = new BufferedReader(file);
			String s;
			while((s = buffer.readLine())!=null){
				wcMR.addKeyValue(0 , s);
			}
		}catch(Exception e){
			System.err.println("ファイル読み込み失敗");
	    }
		
		wcMR.run();				
	}

}
