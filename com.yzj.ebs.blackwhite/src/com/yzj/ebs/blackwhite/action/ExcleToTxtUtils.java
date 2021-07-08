package com.yzj.ebs.blackwhite.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 为了测试 将excle变成dat文件
 * @author Administrator
 *
 */
public class ExcleToTxtUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		createDatFlie("D:\\TestData\\账户信息模板.xls", "D:\\TestData\\MAINDATA.dat", "");
		createDatFlie("D:\\TestData\\账户交易明细模板.xls", "D:\\TestData\\DEPHIST.dat", "");
		
	}
	
	/**
	 * 将excel变成dat文件
	 * @param filePath 输入文件路径
	 * @param toFilePath 输出文件路径
	 * @param splitStr 分隔符
	 */
	public static void createDatFlie(String filePath,String toFilePath,String splitStr){
		Workbook book;
		FileOutputStream fos = null;
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			// 获取文件
			book = Workbook.getWorkbook(fis);
			
			// 读取文件
			Sheet sheet = book.getSheet(0);
			int rows = sheet.getRows(); // 总行数
			int columns = sheet.getColumns(); // 总列数 
			
			if(rows>1){
				fos = new FileOutputStream(new File(toFilePath));
				for(int i=1;i<rows;i++){
					StringBuffer sb = new StringBuffer();
					for(int j=0;j<columns;j++){
						String value=sheet.getCell(j, i).getContents().trim().toString();
						if(value==null||value==""){
							sb.append(" ");
						}else{
							sb.append(value);
						}
						if(j!=columns-1){
							sb.append(splitStr);
						}else{
							if(i!=rows-1){
								sb.append("\n");
							}
						}
					}
					fos.write(sb.toString().getBytes());
				}
			}
		} catch (Exception e) {
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	}

}
