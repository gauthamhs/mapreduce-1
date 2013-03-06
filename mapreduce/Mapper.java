package mapreduce;

import java.util.*;

public abstract class Mapper <InputKey, InputValue, OutputKey, OutputValue>{
	protected InputKey ikey;
	protected InputValue ivalue;
	ArrayList<OutputKey> okeys;
	ArrayList<OutputValue> ovalues;
	
	
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
	
	void setKeyValue(InputKey key, InputValue value){
		this.ikey = key;
		this.ivalue = value;
		
	}
	
	ArrayList<OutputKey>  getKeys(){
		return this.okeys;
	}
	
	ArrayList<OutputValue>  getValues(){
		return this.ovalues;
	}
	
	protected void emit(OutputKey okey, OutputValue ovalue){
		this.okeys.add(okey);
		this.ovalues.add(ovalue);
	}

	protected abstract void map();
}
