package com.yzj.ebs.auto.verifydata;
/**
 * 
 * @author Administrator
 *
 */
public class CompareUtils {
	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compateStr(String str1,String str2){
		
		StringBuffer sb = new StringBuffer(str2);
		if(str1.length()>str2.length()){
			//将str2右边补0
			for(int i=0;i<str1.length()-str2.length();i++){
				sb.append("0");
			}
		}
		return str1.equals(sb.toString());
	}
}
