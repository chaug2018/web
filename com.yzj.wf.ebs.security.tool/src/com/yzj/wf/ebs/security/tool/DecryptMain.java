package com.yzj.wf.ebs.security.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 创建于:2012-12-11<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 解密工具
 * 
 * @author lif
 * @version 1.0
 */
public class DecryptMain {
	
	public static void main(String[] args) {
		String filePath="d:\\20121221_EbillData.txt"; //需要解密的文件路径
		String pwd="111111";   //解密时输入的解密密码
		
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入文件路径：格式比如  d:\\\\20121211_EbillData.txt");
		filePath=sc.nextLine();
		System.out.println("请输入解密密码：");
		pwd=sc.nextLine();
		
		String path = "C:\\" + getNowDate() + "_EbillData.txt"; //解密后生成的文件
		try {
			SecurityTool st=new SecurityTool(SecurityTool.base64);
			File file = new File(filePath);
			BufferedReader reader = null;
			String tempString = null;
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "utf-8"));
			int i=0; 					//标识 i=0 是 第一行的密码 
			//String strLineValue=null; 	//存放解密后的行值
			StringBuffer all_str = new StringBuffer();
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				i++;
				if(i==1){
					String p=st.decrypt(tempString);
					if(!pwd.equals(p)){
						System.out.println("亲，您输入的解密密码不对！");
						break;
					}
				}
				else{
					//在eclipse里运行编码正常
					//strLineValue=new String(st.decrypt(tempString).getBytes("utf-8"),"utf-8");
//					strLineValue=new String(st.decrypt(tempString).getBytes("GBK"),"utf-8");
//					writeTxtFile(path, strLineValue);
					all_str.append(tempString );
				}
			}
			writeTxtFile(path, new String(st.decrypt(all_str.toString()).getBytes("utf-8"),"utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 当前日期
	 * @return
	 */
	public static String getNowDate() {
		Date newdate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String dateformat = sf.format(newdate);
		return dateformat;
	}
	
	/**
	 * 解密后生成明文的文本文件
	 * @param filepath 生成文件路径
	 * @param str 解密后的每行数据
	 */
	public static void writeTxtFile(String filepath,String str) {
		FileWriter filewriter = null;
		try {
			File file = new File(filepath);
			if (file.exists()) {
				file.delete();
				filewriter = new FileWriter(file, true);
			} else {
				if (file.createNewFile()) {
					filewriter = new FileWriter(file, true);
				} else {
					System.out.println("创建文件失败：" + filepath + "/"
							+ filepath);
				}
			}
			if (filewriter != null) {
				try {
					filewriter.write(str + "\r\n");
					filewriter.flush();
				} catch (IOException e) {
					System.out.println("生成对账数据的文本文件失败");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
