package mapreduce;

import java.util.*;

/**
 * 
 * @author takafumi
 * 
 * Map�t�F�[�Y��Suffle�t�F�[�Y�̃f�[�^��ێ��������邽�߂̃N���X
 * 
 * @param <InputMapKey> Map�t�F�[�Y�̓��͂ɂ�����L�[�̃N���X
 * @param <InputMapValue> Map�t�F�[�Y�̓��͂ɂ�����o�����[�̃N���X�@
 * @param <IntermediateKey> Map�t�F�[�Y�̏o�͂ɂ�����L�[�̃N���X
 * @param <IntermediateValue> Map�t�F�[�Y�̏o�͂ɂ�����o�����[�̃N���X
 */
public class InputData<InputMapKey extends Comparable<InputMapKey>, InputMapValue, IntermediateKey extends Comparable<IntermediateKey>, IntermediateValue>{
	List<KeyValue<InputMapKey, InputMapValue>> initialKeyValue;
	List<KeyValue<IntermediateKey , IntermediateValue>> mappedKeyValue;
	List<GroupedKeyValue<IntermediateKey, IntermediateValue>> gKVList;
	
	
	InputData(){
		this.initialKeyValue = new ArrayList<KeyValue<InputMapKey, InputMapValue>>();
		this.mappedKeyValue = new ArrayList<KeyValue<IntermediateKey , IntermediateValue>>();	
		this.gKVList = new ArrayList<GroupedKeyValue<IntermediateKey, IntermediateValue>>();
	}
	
	
	/**
	 * ���[�U����n���ꂽ���̓L�[�o�����[�����X�g�Ɋi�[���邽�߂̊֐�
	 * @param key ���̓L�[
	 * @param value ���̓o�����[
	 */
	void putKeyValue(InputMapKey key, InputMapValue value){
		this.initialKeyValue.add(new KeyValue<InputMapKey, InputMapValue>(key, value));
	}
	
	/**
	 * 
	 * @param inputlist
	 */
	void reloadKeyValueList(List<KeyValue<InputMapKey, InputMapValue>> inputlist){
		this.initialKeyValue = inputlist;
	}
	

	/**
	 * 
	 * @param index
	 * @return
	 */
	InputMapKey getMapKey(int index){
		return this.initialKeyValue.get(index).getKey();
	}
	
	InputMapValue getMapValue(int index){
		return this.initialKeyValue.get(index).getValue();
	}
	
	int getMapSize(){
		return this.initialKeyValue.size();
	}
	

	void setMap(IntermediateKey k, IntermediateValue v){
		this.mappedKeyValue.add(new KeyValue<IntermediateKey, IntermediateValue>(k, v));
	}
	
	/*
	 * Map�t�F�[�Y�̌��key-value��\��
	 */
	void showMap(){
		for(int i = 0; i < mappedKeyValue.size(); i++){
			System.out.println(mappedKeyValue.get(i).getKey().toString() + "," + mappedKeyValue.get(i).getValue().toString());
		}
	}
	
	
/*
 * Suffle 
 */
	
	int getShuffleSize(){
		return this.mappedKeyValue.size();
	}
	
	/*
	 * initial_map��initial_reduce�̃��������
	 */
	
	void initialRelease(){
		this.initialKeyValue = null;
	}
	
/*
 * Map���\�[�g����
 * Key�ɏ]���ă\�[�g���邪������Value�ɂ��Ă��\�[�g���s��	
*/
	void cSort(){
		Collections.sort(this.mappedKeyValue);
	}
	
	void grouping(){
		IntermediateKey tmpKey;
		GroupedKeyValue<IntermediateKey, IntermediateValue> gkv;
		
		
		IntermediateKey conKey = this.mappedKeyValue.get(0).getKey();
		gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(0).getValue()));
		
		for(int i = 1; i < this.mappedKeyValue.size(); i++){
			tmpKey = this.mappedKeyValue.get(i).getKey();
			
			if(tmpKey.equals(gkv.getKey())){
				gkv.addValue(this.mappedKeyValue.get(i).getValue());
			}
			else{
				this.gKVList.add(gkv);
				conKey = tmpKey; 
				gkv = new GroupedKeyValue<IntermediateKey, IntermediateValue>(conKey, new GroupedValues<IntermediateValue>(this.mappedKeyValue.get(i).getValue()));
			}
		}
		this.gKVList.add(gkv);
		
		this.mappedKeyValue = null;
	}

	void showSuffle(){
		for(GroupedKeyValue<IntermediateKey, IntermediateValue> g_kv :gKVList ){
			System.out.print(g_kv.getKey().toString());
			for(IntermediateValue value : g_kv.getValues()){
				System.out.print("," + value.toString());
			}
			System.out.println("");
		}
	}
	
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