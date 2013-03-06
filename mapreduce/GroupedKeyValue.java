package mapreduce;

public class GroupedKeyValue<K, V> {
	K key;
	GroupedValues<V> gValues;

/*
 * Constructor
 */
	GroupedKeyValue(K key){
		this.key = key;
		this.gValues = new GroupedValues<V>();
	}
	
	GroupedKeyValue(K key, GroupedValues<V> values){
		this.key = key;
		this.gValues = values;
	}
	
	/*
	 * ÉfÅ[É^ëÄçÏånMethod
	 */
	
	void setKey(K key){
		this.key = key;
	}
	
	void setValue(V value){
		this.gValues.set(value);
	}
	
	void setValues(GroupedValues<V> gValues){
		this.gValues = gValues;
	}
	
	K getKey(){
		return this.key;
	}
	
	V getValue(){
		return this.gValues.get();
	}
	
	GroupedValues<V> getValues(){
		return this.gValues;
	}
	
	
	
	
}
