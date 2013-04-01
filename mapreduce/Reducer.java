package mapreduce;

/*
 */
/**
 * 
 * @author takafumi
 *
 * MapReduce��Reduce�t�F�[�Y����舵���N���X
 * �A�v���̊J���҂�Reduce�t�F�[�Y�������Ƃ��͂��̃N���X�̃T�u�N���X��reduce���\�b�h�������L�q����Ηǂ��B
 * @param <InputKey> Reduce�t�F�[�Y�̓��̓L�[�̃N���X
 * @param <InputValue> Reduce�t�F�[�Y�̓��̓o�����[�̃N���X
 * @param <InputKey> Reduce�t�F�[�Y�̏o�̓L�[�̃N���X
 * @param <InputKey> Reduce�t�F�[�Y�̏o�̓o�����[�̃N���X
 */
public abstract class Reducer<InputKey, InputValue, OutputKey, OutputValue>{
	/**
	 *@param ikey ���̓L�[ 
	 *@param ivalues ���̓o�����[��GroupedValues�N���X�ŃO���[�v�������g 
	 *@param okey �o�͂����L�[ 
	 *@param ovalue�@�o�͂����o�����[
	 *
	*/
	InputKey ikey;
	GroupedValues<InputValue> ivalues;
	OutputKey okey;
	OutputValue ovalue;
	
	protected Reducer(){
	}
	
	/**
	 * ���͂̃L�[�ƃo�����[�̃Z�b�^�[���\�b�h
	 * @param key ���̓L�[
	 * @param groupedInputValues�@���̓o�����[
	 */
	void setKeyValue(InputKey key, GroupedValues<InputValue> groupedInputValues){
		this.ikey = key;
		this.ivalues = groupedInputValues;
	}	
	/**
	 * 
	 * ���̓L�[�̃Q�b�^�[���\�b�h
	 * �T�u�N���X�ł͂��̃��\�b�h���g�����̓L�[���l������B
	 * @return ���̓L�[
	 */
	protected InputKey getInputKey(){
		return this.ikey;
	}
	/**
	 * 
	 * ���̓o�����[�̃Q�b�^�[���\�b�h
	 * �T�u�N���X�ł͂��̃��\�b�h���g�����̓o�����[�̑g���l������B
	 * @return �O���[�v�������o�̓L�[
	 */
	protected GroupedValues<InputValue> getInputValue(){
		return this.ivalues;
	}

	/**
	 * 
	 * �o�̓L�[�̃Q�b�^�[���\�b�h
	 * @return �o�̓L�[
	 */
	OutputKey getKey(){
		return okey;
	}
	
	/**
	 * 
	 * �o�̓o�����[�̃Q�b�^�[���\�b�h
	 * @return �o�̓L�[
	 */
	OutputValue getValue(){
		return ovalue;
	}
	
	/**
	 * Reduce�t�F�[�Y�̏o�͂���L�[�ƃo�����[��n�����\�b�h
	 * reduce���\�b�h���ŕK��emit���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ�.
	 * @param key �o�̓L�[
	 * @param value �o�̓o�����[
	 */
	protected void emit(OutputKey key, OutputValue value){
		this.okey = key;
		this.ovalue = value;
	}
	
	/**
	 * Reduce�t�F�[�Y�̋������`���郁�\�b�h
	 * �K��emit���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ��B
	 */	
	protected abstract void reduce();
	
}
