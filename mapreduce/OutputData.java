package mapreduce;

import java.util.ArrayList;

public class OutputData<K, V> {
	ArrayList<K> keys;
	ArrayList<V> values;
	
	OutputData(){
		this.keys = new ArrayList<K>();
		this.values = new ArrayList<V>();
	}
	
	void setKeyValue(K key, V value){
		this.keys.add(key);
		this.values.add(value);
	}
	
	void reduceShow(){
		for(int i = 0; i < keys.size(); i++){
			System.out.println(keys.get(i).toString() + ", " + values.get(i).toString());
		}
	}
	
}
