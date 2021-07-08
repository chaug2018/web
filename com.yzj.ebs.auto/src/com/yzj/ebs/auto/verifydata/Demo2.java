package com.yzj.ebs.auto.verifydata;

public class Demo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "994559.68";
		String str1 = "0";
		String resule = str.replace(".","");
		System.out.println(resule);
		if(str1.length()==1 || "0".equals(str1)){
			str1 = "000";
		}
		System.out.println(str1);
	}

}
