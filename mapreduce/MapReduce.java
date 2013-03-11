package mapreduce;

import java.util.*;
import java.util.concurrent.*;

public class MapReduce<InputMapKey, InputMapValue, IntermediateKey extends Comparable<IntermediateKey >, IntermediateValue, OutputReduceKey, OutputReduceValue>{
	
	Class<? extends Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue>> mapClass;
	Class<? extends Reducer< IntermediateKey, IntermediateValue, OutputReduceKey, OutputReduceValue>> reduceClass;
	
	InputData<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> inputData;
	OutputData<OutputReduceKey, OutputReduceValue> outputData;
	
	/*
	 * phase_mp変数でどのフェーズまで実行するかを見極める
	 * phase_mpが"MAP_ONLY"ならばMap処理のみ
	 * "MAP_SHUFFLE"ならばMapとShuffle処理
	 * "MAP_REDUCE"0以上ならばMapとShuffleとReduceの三つを行う
	 * それ以外の値であれば"MAP_REDUCE"として扱う
	 */
	
	String phaseMR;
	
	/*
	 * result_outputはフェーズごとの結果の出力のon/offについて定義する
	 */
	boolean resultOutput;
	
	
	//並列数
	int paralelThreadNum;
	

	
	public MapReduce(Class<? extends Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue>> map_class, Class<? extends  Reducer< IntermediateKey, IntermediateValue, OutputReduceKey, OutputReduceValue>> reduce_class, String phase_mp){
		this.mapClass = map_class;
		this.reduceClass = reduce_class;
		this.inputData = new InputData<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue>();
		this.outputData = new OutputData<OutputReduceKey, OutputReduceValue>();
		this.phaseMR = phase_mp;
		this.resultOutput = true;
		this.paralelThreadNum = 1;
	}
	
	public void setResultOutput(boolean resultOutput){
		this.resultOutput = resultOutput;
	}
	
	public void setParallelThreadNum(int num){
		this.paralelThreadNum = num;
	}
	
	
	/*
	 * addKeyValue
	 * pass data formated as key-value pairs to inputData
	 */
	
	public void addKeyValue(InputMapKey key, InputMapValue value){
		this.inputData.putKeyValue(key, value);
	}
	
	/*
	 * Map関数を実行する
	 * 各Key-Valueに対し以下の処理を行う
	 * 1.Mapperインスタンスの生成
	 * 2.Mapperインスタンスに
	 * 
	 */
	void startMap(){
			Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> local_map;
			ArrayList<IntermediateKey> okeys;
			ArrayList<IntermediateValue> ovalues;
			Executor executor = Executors.newFixedThreadPool(this.paralelThreadNum);
			
			for(int i = 0; i < this.inputData.getMapSize(); i ++){
				try{
					local_map = mapClass.newInstance();
				}catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				
				local_map.setKeyValue(this.inputData.getMapKey(i), this.inputData.getMapValue(i));
				
			//	local_map.map();
				
				executor.execute(local_map);
	
				//Map関数の出力を入れる
				okeys = local_map.getKeys();
				ovalues = local_map.getValues();
				for(int j = 0; j < okeys.size(); j++){
					this.inputData.setMap(okeys.get(j), ovalues.get(j));
				}
				
				//メモリ領域の解放
				okeys = null ;
				ovalues = null;
				local_map = null;
		}
		
		//Map処理を全て終えたらinput_data内の初期値を保存しているメモリ領域を解放
		this.inputData.initialRelease();
	}
	
	void startShuffle(){
//		this.inputData.cSort();
		this.inputData.grouping();
	}
	
	void startReduce(){
		Reducer<IntermediateKey, IntermediateValue,OutputReduceKey, OutputReduceValue> local_reduce;
		for(int i = 0; i < this.inputData.getReduceSize(); i ++){
			try{
				local_reduce = reduceClass.newInstance();
			}catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			
			local_reduce.setKeyValue(this.inputData.getReduceKey(i), this.inputData.getReduceValues(i));
			local_reduce.reduce();
			this.outputData.setKeyValue(local_reduce.getKey(), local_reduce.getValue());
		}
		//メモリ領域解放
		inputData = null;
	}
	
	public void run(){
		startMap();
		if(this.phaseMR.equals("MAP_ONLY")){
			if(this.resultOutput)
				inputData.showMap();
			return;
		}

		startShuffle();
		if(this.phaseMR.equals("MAP_SHUFFLE")){
			if(this.resultOutput)
				inputData.showSuffle();
			return;
		}
		System.out.println("Reduce Phase is finished.");
		startReduce();
		if(this.resultOutput)
			outputData.reduceShow();
	}
	
	
}

