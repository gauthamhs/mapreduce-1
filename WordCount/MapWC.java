package WordCount;

import java.util.*;

import mapreduce.*;

public class MapWC extends Mapper<Integer, String, String, Integer>{
	/**
	 * 1.�o�����[�Ƃ��ė^����ꂽ��������p�[�X����
	 * 2.�P����L�[�A1���o�����[�Ƃ���emit�֐��ɓn��
	 * (non-Javadoc)
	 * @see mapreduce.Mapper#map()
	 */
	protected void map(){
		String line = this.getInputValue();
		StringTokenizer tokenizer = new StringTokenizer(line);
		while(tokenizer.hasMoreTokens()){
			emit(tokenizer.nextToken(), new Integer(1));
		}
	}
}




