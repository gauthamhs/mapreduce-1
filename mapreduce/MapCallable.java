package mapreduce;

import java.util.concurrent.Callable;


/**
 * 
 * @author takafumi
 *
 * Mapper�N���X�����Ɏ��s���邽�߂�Callable�����������N���X
 * 
 * @param <InputMapKey> Map�̓��̓L�[�̃N���X
 * @param <InputMapValue> Map�̓��̓o�����[�̃N���X
 * @param <IntermediateKey>�@Map�̏o�̓L�[�̃N���X
 * @param <IntermediateValue> Map�̏o�̓o�����[�̃N���X
 */
public class MapCallable<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> implements Callable<Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue>>{
	Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> mapper;
	
	MapCallable(){
	}
	
	MapCallable(Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> mapper){
		this.mapper = mapper;
	}
	
	public Mapper<InputMapKey, InputMapValue, IntermediateKey, IntermediateValue> call(){
		this.mapper.map();
		return this.mapper;
	}
}
