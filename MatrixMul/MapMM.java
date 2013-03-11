package MatrixMul;

import java.util.*;
import mapreduce.*;

public class MapMM extends Mapper<Integer, double[], Integer, Double>{
	
	protected void map(){
		emit(this.ikey, ivalue[0]*ivalue[1]);
	}
}
