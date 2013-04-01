package mapreduce;

import java.util.*;

/*
 * MapReduce��Map�t�F�[�Y����舵���N���X
 * �A�v���̊J���҂�Map�t�F�[�Y�������Ƃ��͂��̃N���X�̃T�u�N���X��map���\�b�h�������L�q����Ηǂ��B
 * @param <InputKey> Map�t�F�[�Y�̓��̓L�[�̃N���X
 * @param <InputValue> Map�t�F�[�Y�̓��̓o�����[�̃N���X
 * @param <InputKey> Map�t�F�[�Y�̏o�̓L�[�̃N���X
 * @param <InputKey> Map�t�F�[�Y�̏o�̓o�����[�̃N���X
 */
public abstract class Mapper <InputKey, InputValue, OutputKey, OutputValue> {
	/*
	 *@param ikey ���̓L�[ 
	 *@param ivalue ���̓L�[ 
	 *@param okey �o�͂����L�[ 
	 *@param ovalue�@�o�͂����o�����[
	 *
	 * okey��ovalue�̓��X�g�ł��蓯���g�̃L�[�o�����[�͓���̃C���f�b�N�X�Ɋi�[����Ă���B
	*/
	private InputKey ikey;
	private InputValue ivalue;
	private List<OutputKey> okeys;
	private List<OutputValue> ovalues;
		
	protected Mapper(){
		this.okeys = new ArrayList<OutputKey>();
		this.ovalues = new ArrayList<OutputValue>();
	}
	
	Mapper(InputKey key, InputValue value){
		this.ikey = key;
		this.ivalue = value;
		this.okeys = new ArrayList<OutputKey>();
		this.ovalues = new ArrayList<OutputValue>();
	}
	
	/*
	 * ���͂̃L�[�ƃo�����[�̃Z�b�^�[���\�b�h
	 * @param key ���̓L�[
	 * @param value�@���̓o�����[
	 */
	void setKeyValue(InputKey key, InputValue value){
		this.ikey = key;
		this.ivalue = value;
	}
	
	/*
	 * ���̓L�[�̃Q�b�^�[���\�b�h
	 * �T�u�N���X�ł͂��̃��\�b�h���g�����̓L�[���l������B
	 */
	protected InputKey getInputKey(){
		return  this.ikey;
	}
	
	/*
	 * ���̓o�����[�̃Q�b�^�[���\�b�h
	 * �T�u�N���X�ł͂��̃��\�b�h���g�����̓o�����[���l������B
	 */
	protected InputValue getInputValue(){
		return  this.ivalue;
	}
	
	/*
	 * �o�̓L�[�̃Q�b�^�[���\�b�h
	 */
	List<OutputKey> getKeys(){
		return this.okeys;
	}
	
	/*
	 * �o�̓o�����[�̃Q�b�^�[���\�b�h
	 */	
	List<OutputValue>  getValues(){
		return this.ovalues;
	}
	
	/*
	 * Map�t�F�[�Y�̏o�͂���L�[�ƃo�����[��n�����\�b�h
	 * map���\�b�h���ŕK��emit���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ�. 
	 * @param key �o�̓L�[
	 * @param value �o�̓o�����[
	 */
	protected void emit(OutputKey okey, OutputValue ovalue){
		this.okeys.add(okey);
		this.ovalues.add(ovalue);
	}
	
	/*
	 * Map�t�F�[�Y�̋������`���郁�\�b�h
	 * �K��emit���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ��B
	 */
	protected abstract void map();
	
}
