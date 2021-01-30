package com.sbs.untact.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Util {
	public static String getNowDate() {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		
		Date time = new Date();
				
		String regDate = format1.format(time);
		
		return regDate;
	}
	public static Map<String, Object> mapOf(Object...args){
		int size = args.length / 2;
		if(args.length % 2 != 0) {
			throw new IllegalArgumentException("인자를 짝수개 입력해주세요");
		}
		for(int i = 0; i < size; i++) {
			int keyIndex =  i * 2;
			int valueIndex = keyIndex + 1;
			String key;
			Object value;
			try {
				key = (String)args[keyIndex];
			}
			catch (ClassCastException e){
				throw new IllegalArgumentException("키는 문자로 입력해주세요" + e.getMessage());	
			}
			
		}
		
		return null;
	}
	
}
