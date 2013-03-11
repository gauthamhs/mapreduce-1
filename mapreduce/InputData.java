package mapreduce;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public class InputData<InputMapKey, InputMapValue, IntermediateKey extends Comparable<IntermediateKey> , IntermediateValue>{
	List<InputMapKey> initialKeys;
	List<InputMapValue> initialValues;
	List<KeyValue<IntermediateKey , IntermediateValue>> mappedKeyValue;
	//List<GroupedKeyValue<IntermediateKey, IntermediateValue>> gKVs;
	ConcurrentHashMap<IntermediateKey, GroupedValues<IntermediateValue>> gKVMap;
	List<GroupedKeyValue<IntermediateKey, IntermediateValue>> gKVList;
	
	
	/*
	 * Constructor
	 */
	
	InputData(){
		this.initialKeys = new ArrayList<InputMapKey>();
		this.initialValues = new ArrayList<InputMapValue>();
		this.mappedKeyValue = new ArrayList<KeyValue<IntermediateKey , IntermediateValue>>();	
	//	this.gKVs = new ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>>();
		this.gKVMap = new ConcurrentHashMap<IntermediateKey, GroupedValues<IntermediateValue>>();
		this.gKVList = new ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>>();
	}
	
	/*
	 * データ操作Method
	 */
	
	void putKeyValue(InputMapKey key, InputMapValue value){
		this.initialKeys.add(key);
		this.initialValues.add(value);
	}
	
/*
 * Map
 */
	
	InputMapKey getMapKey(int index){
		return this.initialKeys.get(index);
	}
	
	InputMapValue getMapValue(int index){
		return this.initialValues.get(index);
	}
	
	int getMapSize(){
		return this.initialKeys.size();
	}
	
	/*
	void setMap(IntermediateKey k, IntermediateValue v){
		this.mappedKeyValue.add(new KeyValue<IntermediateKey, IntermediateValue>(k, v));
	}
	
	void setMap(IntermediateKey k, IntermediateValue v){
		if(!this.gKVMap.containsKey(k)){
			this.gKVMap.put(k, new GroupedValues<IntermediateValue>(v));
		}
		else{
			GroupedValues<IntermediateValue> gVs = this.gKVMap.get(k);
			gVs.add(v);
			this.gKVMap.put(k, gVs);
		}
	}
	
	*/

	void setMap(IntermediateKey k, IntermediateValue v){
		if(this.gKVMap.putIfAbsent(k, new GroupedValues<IntermediateValue>(v)) != null){
			GroupedValues<IntermediateValue> gv = this.gKVMap.get(k);
			gv.add(v);
			while(!this.gKVMap.replace(k, this.gKVMap.get(k), gv)){
				gv = this.gKVMap.get(k);
				gv.add(v);
			}
		}
	}
	
	
	
	/*
	 * Mapフェーズの後のkey-valueを表示
	 */
	void showMap(){
		for(int i = 0; i < mappedKeyValue.size(); i ++){
			System.out.println(mappedKeyValue.get(i).getKey().toString() + "," + mappedKeyValue.get(i).getValue().toString());
		}
	}
	
	
/*
 * Suffle 
 */
	
	int getShuffleSize(){
		return this.mappedKeyValue.size();
//		return this.mappedKeys.size();
	}
	
	/*
	 * initial_mapとinitial_reduceのメモリ解放
	 */
	
	void initialRelease(){
		this.initialKeys = null;
		this.initialValues = null;
	}
	
/*
 * Mapをソートする
 * Keyに従ってソートするが同時にValueについてもソートを行う	
 */
	void cSort(){
		Collections.sort(this.mappedKeyValue);
	}
/*
	void grouping(){
		 //group values having same key to GroupedValues Class.
		IntermediateKey tmpKey;
		GroupedKeyValue<IntermediateKey, IntermediateValue> gkv;
		
		
		// 先頭のKeyを現在のKey(con_key)として獲得しg_kvにいれる
		IntermediateKey conKey = this.mappedKeyValue.get(0).getKey();
		gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(0).getValue()));
		
		for(int i = 1; i < this.mappedKeyValue.size(); i++){
			tmpKey = this.mappedKeyValue.get(i).getKey();
			
			if(tmpKey.equals(gkv.getKey())){
				gkv.addValue(this.mappedKeyValue.get(i).getValue());
			}
			else{
				this.gKVs.add(gkv); // g_kvsに登録
				conKey = tmpKey; //次のcon_keyを定義
				gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(i).getValue()));
			}
		}
		this.gKVs.add(gkv);
		
		 //mapped_keysとmapped_valuesのメモリを解放する
		this.mappedKeyValue = null;
	}
	
*/	
	void grouping(){
		for(Entry<IntermediateKey, GroupedValues<IntermediateValue>> g_kv :gKVMap.entrySet() ){
			this.gKVList.add(new GroupedKeyValue<IntermediateKey, IntermediateValue>(g_kv.getKey(), g_kv.getValue()));
		}
	}

	
	/*
	 * Shuffleでの結果を表示
	 */
	void showSuffle(){
		//for(GroupedKeyValue<IntermediateKey, IntermediateValue> g_kv :gKVs ){
		for(Entry<IntermediateKey, GroupedValues<IntermediateValue>> g_kv :gKVMap.entrySet() ){
			System.out.print(g_kv.getKey().toString());
			for(IntermediateValue value : g_kv.getValue()){
				System.out.print("," + value.toString());
			}
			System.out.println("");
		}
	}
	
/*
 * Reduce
 */
	IntermediateKey getReduceKey(int index){
		return this.gKVList.get(index).getKey();
	}
	
	GroupedValues<IntermediateValue> getReduceValues(int index){
		return this.gKVList.get(index).getValues();
	}
	
	int getReduceSize(){
		return this.gKVList.size();
	}

}