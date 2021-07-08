package com.yzj.ebs.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat fromatDateFormat  =new SimpleDateFormat("yyyy-MM-dd");
		String  str = fromatDateFormat.format(new Date());
		System.out.println(str);
	
	}

}
