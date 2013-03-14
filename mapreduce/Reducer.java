package mapreduce;

public abstract class Reducer<InputKey, InputValue, OutputKey, OutputValue>{
	protected InputKey ikey;
	protected GroupedValues<InputValue> ivalues;
	OutputKey okey;
	OutputValue ovalue;
	
	protected Reducer(){
	}
	
	
	void setKeyValue(InputKey key, GroupedValues<InputValue> groupedInputValues){
		this.ikey = key;
		this.ivalues = groupedInputValues;
	}
	

	OutputKey getKey(){
		return okey;
	}
	
	OutputValue getValue(){
		return ovalue;
	}
	
		
	protected void emit(OutputKey key, OutputValue value){
		this.okey = key;
		this.ovalue = value;
	}
	
	protected abstract void reduce();
	

}
