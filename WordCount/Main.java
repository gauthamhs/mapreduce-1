package WordCount;

import mapreduce.*;
import java.io.*;

public class Main {

	/**
	 * @param args
	 */
	
	/*
	 * WordCount
	 * �����ŗ^����ꂽ�t�@�C�����ɏo�Ă��镶���̐��𐔂���
	 * �����Ńt�@�C�����^�����Ȃ��ꍇ�͂��̃\�[�X�R�[�h���̕��������J�E���g����
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * wordcount���s���t�@�C���̎w��
		 * �t�@�C���������ꍇ�͂��̃\�[�X�R�[�h�ɑ΂�wordcount���s��
		 */
		String filename;
		if(args.length > 1){
			filename = args[2];
		}
		else{
			filename = "/Users/saitoutakafumi/Dropbox/workspace/mapreduce_java/src/mapreduce/mapreduce/MapReduce.java";
		}
			
		MapReduce<Integer, String, String, Integer, String, Integer> wcMR = new MapReduce(MapWC.class, ReduceWC.class, "MAP_REDUCE");

		
		//�����l��MapReduce�ɓn��
		try{
			FileReader file = new FileReader(filename);
			BufferedReader buffer = new BufferedReader(file);
			String s;
			while((s = buffer.readLine())!=null){
				wcMR.addKeyValue(0 , s);
			}
		}catch(Exception e){
			System.out.println("�t�@�C���ǂݍ��ݎ��s");
	    }
		
		wcMR.run();				
	}

}
