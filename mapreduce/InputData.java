package mapreduce;

import java.util.*;

public class InputData<InputMapKey, InputMapValue, IntermediateKey extends Comparable<IntermediateKey> , IntermediateValue>{
	ArrayList<InputMapKey> initialKeys;
	ArrayList<InputMapValue> initialValues;
	ArrayList<IntermediateKey> mappedKeys;
	ArrayList<IntermediateValue> mappedValues;
	ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>> gKVs;
	
	
	/*
	 * Constructor
	 */
	
	InputData(){
		this.initialKeys = new ArrayList<InputMapKey>();
		this.initialValues = new ArrayList<InputMapValue>();
		this.mappedKeys = new ArrayList<IntermediateKey>();
		this.mappedValues = new ArrayList<IntermediateValue>();		
		this.gKVs = new ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>>();
	}
	
	/*
	 * �f�[�^����Method
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
	
	void setMap(IntermediateKey k, IntermediateValue v){
		this.mappedKeys.add(k);
		this.mappedValues.add(v);
	}

	void setMap(IntermediateKey k, IntermediateValue v, int index){
		this.mappedKeys.add(index, k);
		this.mappedValues.add(index, v);
	}
	
	/*
	 * Map�t�F�[�Y�̌��key-value��\��
	 */
	void showMap(){
		for(int i = 0; i < mappedKeys.size(); i ++){
			System.out.println(mappedKeys.get(i).toString() + "," + mappedValues.get(i).toString());
		}
	}
	
	
/*
 * Suffle 
 */
	
	int getShuffleSize(){
		return this.mappedKeys.size();
	}
	
	/*
	 * initial_map��initial_reduce�̃��������
	 */
	
	void initialRelease(){
		this.initialKeys = null;
		this.initialValues = null;
	}
	
/*
 * Map���\�[�g����
 * Key�ɏ]���ă\�[�g���邪������Value�ɂ��Ă��\�[�g���s��	
 */
	void qSort(int left, int right){
		int i = left;
		int j = right;
		IntermediateKey p = this.mappedKeys.get((left + right) / 2);
	
		while(true){
			while(this.mappedKeys.get(i).compareTo(p) < 0) i++;
			while(this.mappedKeys.get(j).compareTo(p) > 0) j--;
			
			if(i >= j) break;
			
			swapKeyValue(i, j);
			i++;
			j--;
			
			if(left < i - 1) qSort(left, i - 1);
			if(j + 1 < right) qSort(j + 1, right);
				
		}
		
	}
	
	/*
	 * Key��Value�����ꂼ���������
	 */
	void swapKeyValue(int i, int j){
		IntermediateKey tmpkey;
		IntermediateValue tmpvalue;
			
		tmpkey = this.mappedKeys.get(i);
		this.mappedKeys.set(i, this.mappedKeys.get(j));
		this.mappedKeys.set(j, tmpkey);
		
		tmpvalue = this.mappedValues.get(i);
		this.mappedValues.set(i, this.mappedValues.get(j));
		this.mappedValues.set(j, tmpvalue);
	}
	
	void grouping(){
		/*
		 * keys��O����Ȃ߂ē���Key������Value��GroupedValues�ɃO���[�s���O����
		 */
		IntermediateKey tmpKey;
		GroupedKeyValue<IntermediateKey, IntermediateValue> gkv;
		
		
		//�擪��Key�����݂�Key(con_key)�Ƃ��Ċl����g_kv�ɂ����
		IntermediateKey con_key = this.mappedKeys.get(0);
		gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(con_key, new GroupedValues<IntermediateValue>(this.mappedValues.get(0)));
		
		for(int i = 1; i < this.mappedKeys.size(); i++){
			tmpKey = this.mappedKeys.get(i);
			if(tmpKey.equals(gkv.getKey())){
				gkv.setValue(this.mappedValues.get(i));
			}
			else{
				this.gKVs.add(gkv); // g_kvs�ɓo�^
				con_key = tmpKey; //����con_key���`
				gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(con_key, new GroupedValues<IntermediateValue>(this.mappedValues.get(i)));
			}
		}
		this.gKVs.add(gkv);
		
		/*
		 * mapped_keys��mapped_values�̃��������������
		 */
		this.mappedKeys = null;
		this.mappedValues = null;
	}
	
	/*
	 * Shuffle�ł̌��ʂ�\��
	 */
	void showSuffle(){
		for(GroupedKeyValue<IntermediateKey, IntermediateValue> g_kv :gKVs ){
			System.out.print(g_kv.getKey().toString());
			for(IntermediateValue value : g_kv.getValues()){
				System.out.print("," + value.toString());
			}
			System.out.println("");
		}
	}
	
/*
 * Reduce
 */
	IntermediateKey getReduceKey(int index){
		return this.gKVs.get(index).getKey();
	}
	
	GroupedValues<IntermediateValue> getReduceValues(int index){
		return this.gKVs.get(index).getValues();
	}
	
	int getReduceSize(){
		return this.gKVs.size();
	}

}