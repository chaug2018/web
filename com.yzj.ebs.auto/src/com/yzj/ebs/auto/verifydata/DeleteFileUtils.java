package com.yzj.ebs.auto.verifydata;

import java.io.File;

/**
 * 删除文件
 * @author Administrator
 *
 */
public class DeleteFileUtils {
	
	/**
	 * 删除filePath
	 */
	public static void DeleteFile(String filePath){
		try{
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
		}catch(Exception e){
			
		}
	}
}
