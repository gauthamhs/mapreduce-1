package mapreduce;

import java.util.*;

public class InputData<InputMapKey, InputMapValue, IntermediateKey extends Comparable<IntermediateKey> , IntermediateValue>{
	ArrayList<InputMapKey> initialKeys;
	ArrayList<InputMapValue> initialValues;
	ArrayList<KeyValue<IntermediateKey , IntermediateValue>> mappedKeyValue;
	ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>> gKVs;
	
	
	/*
	 * Constructor
	 */
	
	InputData(){
		this.initialKeys = new ArrayList<InputMapKey>();
		this.initialValues = new ArrayList<InputMapValue>();
		this.mappedKeyValue = new ArrayList<KeyValue<IntermediateKey , IntermediateValue>>();	
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
		this.mappedKeyValue.add(new KeyValue<IntermediateKey, IntermediateValue>(k, v));
	}
	
	/*
	 * Map�t�F�[�Y�̌��key-value��\��
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
	void cSort(){
		Collections.sort(this.mappedKeyValue);
	}

	void grouping(){
		/*
		 * group values having same key to GroupedValues Class.
		 */
		IntermediateKey tmpKey;
		GroupedKeyValue<IntermediateKey, IntermediateValue> gkv;
		
		
		/*
		 * �擪��Key�����݂�Key(con_key)�Ƃ��Ċl����g_kv�ɂ����
		 * 
		 */
		IntermediateKey conKey = this.mappedKeyValue.get(0).getKey();
		gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(0).getValue()));
		
		for(int i = 1; i < this.mappedKeyValue.size(); i++){
			tmpKey = this.mappedKeyValue.get(i).getKey();
			
			if(tmpKey.equals(gkv.getKey())){
				gkv.addValue(this.mappedKeyValue.get(i).getValue());
			}
			else{
				this.gKVs.add(gkv); // g_kvs�ɓo�^
				conKey = tmpKey; //����con_key���`
				gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(i).getValue()));
			}
		}
		this.gKVs.add(gkv);
		
		/*
		 * mapped_keys��mapped_values�̃��������������
		 */
		this.mappedKeyValue = null;
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