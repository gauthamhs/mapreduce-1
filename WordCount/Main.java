package WordCount;

import mapreduce.*;
import java.io.*;

public class Main {

	/**
	 * WordCount
	 * �����ŗ^����ꂽ�t�@�C�����ɏo�Ă���P��̐��𐔂���
	 * �����Ńt�@�C�����^�����Ȃ��ꍇ�͂��̃\�[�X�R�[�h���̒P�ꐔ���J�E���g����
	 * @param args �������Ƀt�@�C���̃p�X���i�[���邽�߂̔z��
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename;
		if(args.length > 1){
			filename = args[2];
		}
		else{
			filename = "";
		}
			
		MapReduce<Integer, String, String, Integer, String, Integer> wcMR = new MapReduce<Integer, String, String, Integer, String, Integer>(MapWC.class, ReduceWC.class, "MAP_REDUCE");
		wcMR.setParallelThreadNum(6);
		
		//�����l��MapReduce�ɓn��
		try{
			FileReader file = new FileReader(filename);
			BufferedReader buffer = new BufferedReader(file);
			String s;
			while((s = buffer.readLine())!=null){
				wcMR.addKeyValue(0 , s);
			}
		}catch(Exception e){
			System.err.println("�t�@�C���ǂݍ��ݎ��s");
	    }
		
		wcMR.run();				
	}

}
