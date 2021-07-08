package com.yzj.ebs.auto.datasave;

import java.io.File;
import java.util.TimerTask;

import com.yzj.ebs.auto.datasave.util.DataSaveTools;

public class TimerTaskWare extends TimerTask {

	public TimerTaskWare() {
		super();
	}

	@Override
	public void run() {
		try{
			System.out.println("我执行一次了 ！");
			
			/**
			 * 1、判断 没有做完备份 
			 * 		1.1 执行备份程序
			 */
			
			//1.读取dat文件 获取dataFilePath、wfFilePath的值
			String[] filePath = DataSaveTools.getFilePathFormBat(System.getProperty("user.dir")+File.separator+"public.bat");
			String dataFilePath = filePath[0];
			String wfFilePath = filePath[1];
			
			if(!DataSaveTools.isdataSave(dataFilePath,wfFilePath)){
				System.out.println("当月没有备份 开始做备份 ！");
				
				//1.修改public.bat文件  修改文件名
				DataSaveTools.copyFile(System.getProperty("user.dir")+File.separator+"public.bat");
				//2.删除Public.bat 将生成的时间的Bat改名
				DataSaveTools.deletePublicAndRename();
				
				
				//2.如果执行完备份，则执行删除功能
				if(dataSaveThread()){
					deleteSaveFile(dataFilePath,wfFilePath);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	/**
	 * 删除备份文件
	 * 		如果当前备份文件夹中有8个文件，则删除
	 * 			d:\dataSave\wf
	 * 
	 */
	private void deleteSaveFile(String dataFilePath,String wfFilePath) {
		// TODO Auto-generated method stub
		if(DataSaveTools.isHave8File(dataFilePath)){
			//执行删除   删除时间最前的文件
			DataSaveTools.deleteFileForEarly(dataFilePath);
		}
		
		if(DataSaveTools.isHave8File(wfFilePath)){
			//执行删除  删除时间最前的文件
			DataSaveTools.deleteFileForEarly(wfFilePath);
		}
	}

	/**
	 * 备份程序
	 * 		备份data、wf数据库
	 */
	private boolean dataSaveThread(){
		boolean  result = false;
		try{	
			//执行备份逻辑
			
			//1.执行备份
			String path = System.getProperty("user.dir")+File.separator+"public.bat"; 
		    Runtime run = Runtime.getRuntime();   
		    try {     
		    	 run.exec("cmd.exe /c start " + path);
		    } catch (Exception e) {            
		        e.printStackTrace();   
		    }
		    result =true;
		}catch(Exception e){
			result =false;
			e.printStackTrace();
		}finally{
			return result;
		}
	}
}
