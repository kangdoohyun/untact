package com.sbs.untact.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
	public static String getNowDate() {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		
		Date time = new Date();
				
		String regDate = format1.format(time);
		
		return regDate;
	}
	public static Map<String, Object> mapOf(Object...args){
		if(args.length % 2 != 0) {
			throw new IllegalArgumentException("인자를 짝수개 입력해주세요");
		}
		int size = args.length / 2;
		
		Map<String, Object> map = new LinkedHashMap<>();
		
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
			value = args[valueIndex];

			map.put(key, value);
		}
		
		return map;
	}
	public static int getAsInt(Object object, int defaultValue) {
		if (object instanceof BigInteger) {
			return ((BigInteger) object).intValue();
		} else if (object instanceof Double) {
			return (int) Math.floor((double) object);
		} else if (object instanceof Float) {
			return (int) Math.floor((float) object);
		} else if (object instanceof Long) {
			return (int) object;
		} else if (object instanceof Integer) {
			return (int) object;
		} else if (object instanceof String) {
			return Integer.parseInt((String) object);
		}

		return defaultValue;
	}
	public static String msgAndBack(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back();");
		sb.append("</script>");
		
		return sb.toString();
	}
	public static String msgAndReplace(String msg, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("location.replace('" + url + "');");
		sb.append("</script>");
		
		return sb.toString();
	}
	
}
