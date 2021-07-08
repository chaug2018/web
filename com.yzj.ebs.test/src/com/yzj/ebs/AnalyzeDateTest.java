package com.yzj.ebs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AnalyzeDateTest {
	public List<String> analyzeDate(String str,String splitchar){
		List<String> result = new ArrayList<String>();
		str=str.replace(splitchar+splitchar, splitchar+" "+splitchar+" ");
		String order =str;
		System.out.println(str);
		while(str.length()>0){
			int index = str.indexOf(splitchar);
			String value="";
			if(index>0){
				value = str.substring(0,index);
				str=str.substring(index+1,str.length());
			}else{
				value=str;
				str="";
			}
			result.add(value);
		}
		if(order.lastIndexOf(splitchar)==(order.length()-1)){
			result.add(" ");
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		AnalyzeDateTest dp = new AnalyzeDateTest();
//		String str= "111@11@@@@@@1@";
//		dp.analyzeDate(str, "@");
		dp.deleteDataFile();
		
	}
	
	public void deleteDataFile(){
		//删除三个月之前的数据文件
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-4);
			Date dateFlag = cal.getTime();
			File fileDir = new File("D:/dataFile");
			if(fileDir.isDirectory()){
				//得到所有的年目录
				File[] yearFiles =fileDir.listFiles();
				for(File yearFile:yearFiles){
					String year=yearFile.getName();
					if(year.length()==4){
						if(yearFile.isDirectory()){
							//得到年目录下的所有的月
							File[] mouthFiles =yearFile.listFiles();
							//如果年目录中没有文件，则删除这个年目录
							if(mouthFiles.length==0){
								yearFile.delete();
								continue;
							}
							for(File mouthFile:mouthFiles){
								String day =mouthFile.getName();
								Date fileDate = sf.parse(year+day);
								if(fileDate.getTime()<dateFlag.getTime()){
									deleteFile(mouthFile);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFile(File file){
		try {
			if(file.exists()){
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					File[] files = file.listFiles();
					for(File fileTemp:files){
						deleteFile(fileTemp);
					}
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
