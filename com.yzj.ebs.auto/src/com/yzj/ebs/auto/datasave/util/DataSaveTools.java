package com.yzj.ebs.auto.datasave.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据备份工具类
 * @author Administrator
 *
 */
public class DataSaveTools {
	
	
	/**
	 * 加载此文件夹 判断是否有八个文件
	 * 		没有此文件夹 则创建此文件夹
	 * @param filePath  格式 d://data
	 * @return
	 */
	public static boolean isHave8File(String filePath){
		boolean result = false;
		
		File file = new File(filePath);
		
		//判断
		if(file.list().length >=7){
			result = true;
		}
		return result;
	}
	
	
	/**
	 * 创建时间
	 * @return
	 */
	public static String makeTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		return format.format(new Date());
	}
	
	/**
	 * 是否执行过备份
	 * 	判断此两个文件夹中是否有当月文件即可
	 * 		1.如果此两个文件夹都不存在则创建
	 * 		2.在执行判断
	 * 	
	 * @param datafilepath 
	 * @param wffilepath
	 * @return
	 */
	public static boolean isdataSave(String datafilepath, String wffilepath) {
		File dataFile = new File(datafilepath);
		File wfFile = new File(wffilepath);
		
		//1.如果此文件夹不存在  则创建此文件夹
		if(!dataFile.exists()){
			dataFile.mkdirs();
		}
		if(!wfFile.exists()){
			wfFile.mkdirs();
		}
		
		boolean bool_data =false;
		boolean bool_wf =false;
		//2.必须两个文件都存在才返回真，否则都算没有执行完
		File[] filedatalist = dataFile.listFiles();
		File[] filewfList = wfFile.listFiles();
		//判断
		for(int i=0;i<filedatalist.length;i++){
			if(filedatalist[i].getName().contains(makeTime())==true){
				bool_data = true;
				break;				
			}
		}
		for(int i=0;i<filewfList.length;i++){
			if(filewfList[i].getName().contains(makeTime())==true){
				bool_wf = true;
				break;				
			}
		}
		
		return bool_data&&bool_wf;
	}
	
	/**
	 * 生成文件名
	 * 		fileName格式： data
	 * 		生成的格式： data_yyyyMM.dmp
	 * @return
	 */
	public static String makeFileName(String fileName){
		return fileName+"_"+makeTime()+".dmp";
	}
	
	/**
	 *	更改文件名
	 *		将file的文件名更改为fileName的值
	 *		fileName 
	 */
	public static void renameFileName(File file,String fileName){
		file.renameTo(new File(file.getParent()+File.separator+fileName));
	}
	
	/**
	 * 删除此路径下 文件时间最早的文件
	 * @param filePath
	 */
	public static  void deleteFileForEarly(String filePath){
		File dataFile = new File(filePath);
		File[] filelist = dataFile.listFiles();
		File earlyFile = null;
		String earlyTime = null;
		for(int i=0;i<filelist.length;i++){
			String[] strTep = filelist[i].getName().split("_");
			String[] strTemp = strTep[1].split("\\.");
			if(earlyTime==null || Integer.valueOf(earlyTime)>Integer.valueOf(strTemp[0])){
				//当前保存的小于 strTemp 则将最小的保存下来 ；最早的文件路径保存
				earlyTime = strTemp[0];
				earlyFile = filelist[i];
			}
		}
		
		//执行删除
		if(earlyFile!=null){
			earlyFile.delete();
		}
	}

	/**
	 *  获取此文件中 dataFilePath路径、wfFilePath路径
	 * @param string
	 * @return
	 */
	public static String[] getFilePathFormBat(String filePath) {
		
		String[] resultSts=new String[2];
		BufferedReader reader = null;
		try {
			reader = new  BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
			String str =reader.readLine();
		
			while(str!=null){
				if(str.contains("file=")){
					int top = str.indexOf("file=")+5;
					String path = str.substring(top);
					if(resultSts[0]==null){
						resultSts[0] = path.substring(0, path.lastIndexOf("\\"));
					}else{
						resultSts[1] = path.substring(0, path.lastIndexOf("\\"));
					}
				}
				str =reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return resultSts;
	}

	/**
	 * 将public.bat文件复制一个temp文件
	 * @param filePath
	 */
	public static void copyFile(String filePath) {
		
		BufferedReader reader = null;
		FileOutputStream out = null;
		try{
			reader = new  BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
			out = new FileOutputStream(new File(System.getProperty("user.dir")+File.separator+"temp.bat"));
			String str =reader.readLine();
		
			while(str!=null){
				if(str.contains("file=")){
					String fileFirst = str.substring(0, str.lastIndexOf("\\"));
					
					if(str.contains("wf")){
						str = fileFirst+File.separator+makeFileName("wf")+System.getProperty("line.separator");
					}else{
						str = fileFirst+File.separator+makeFileName("data")+System.getProperty("line.separator");
					}
				}else{
					str = str+System.getProperty("line.separator");
				}
				out.write(str.getBytes());
				str =reader.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(reader!=null){
					reader.close();
				}
				if(out!=null){
					out.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 删除public文件，将temp文件更名为public 
	 */
	public static void deletePublicAndRename() {
		File publicFile = new File(System.getProperty("user.dir")+File.separator+"public.bat");
		File tempFile = new File(System.getProperty("user.dir")+File.separator+"temp.bat");
		publicFile.delete();
		renameFileName(tempFile,"public.bat");
	}
}
