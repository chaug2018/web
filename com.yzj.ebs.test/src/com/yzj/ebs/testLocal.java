package com.yzj.ebs;

import java.io.InputStream;

import com.yzj.common.file.service.FileOperatorService;
import com.yzj.common.file.service.IFileOperator;


public class testLocal {
	public static void main(String[] args) {
		try {
			IFileOperator fo = new FileOperatorService();  
//			InputStream in = fo.get("ftp://yzj:yzj@10.1.90.176/cc/5.txt");
//			InputStream in = fo.get("sftp://yzj:yzj@10.1.90.176/cc/5.txt");tiger
			InputStream in = fo.get("D:\\�ҵ��ĵ�\\һ��java_FTP�Ŀͻ���.docx"); 
			fo.put(in,"D:\\FTP\\һ��java_FTP�Ŀͻ���.docx");
			fo.closeInputStream(in);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
