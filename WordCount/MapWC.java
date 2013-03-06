package WordCount;

import java.util.*;

import mapreduce.*;

public class MapWC extends Mapper<Integer, String, String, Integer>{

	protected void map(){
		String line = this.ivalue;
		StringTokenizer tokenizer = new StringTokenizer(line);
		while(tokenizer.hasMoreTokens()){
			emit(tokenizer.nextToken(), new Integer(1));
		}
	}
}




