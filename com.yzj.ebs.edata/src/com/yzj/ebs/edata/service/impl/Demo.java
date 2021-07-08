package com.yzj.ebs.edata.service.impl;

import java.io.ObjectInputStream.GetField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
//        System.out.println(format.format(calendar.getTime()));
		
//		String str = "asfd|@|232|@|fds|@|232|@||@|343|@|";
//		
//		HashMap<String, String> catchMap = new HashMap<String, String>();
//		catchMap.put("a|@|232", "asfd|@|232|@|fds|@|232|@||@|343|@|");
//		catchMap.put("b|@|231", "asfd|@|232|@||@|232|@||@|343|@|");
//		catchMap.put("c|@|233", "asfd|@|232|@|fds|@|232|@||@|343|@|");
//		catchMap.put("d|@|234", "asfd|@||@|fds|@|232|@||@|343|@|232|@||@|343|@|232|@||@|343");
//		
//		for(Map.Entry<String, String> entry : catchMap.entrySet()){
//			String entryValue = entry.getValue();
//			String[] entrySplits = entryValue.split("\\|@\\|");
//			if(entrySplits.length<12){
//				//不全
//				for(int i=entrySplits.length;i<12;i++){
//					entryValue = entryValue+"\\N|@|";
//				}
//				entryValue = entryValue.substring(0, entryValue.length()-3);
//			}
//			//补上回车
//			entryValue = entryValue+"\r\n";
//			
//			System.out.println(entryValue);
//		}
	
		
	}

}
