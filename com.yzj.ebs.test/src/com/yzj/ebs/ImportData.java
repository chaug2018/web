package com.yzj.ebs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ImportData {

	public void queryDataFromOrcl(){
//		String sq="select accNo from EBS_ACCNOMAINDATA t where t.sendmode='2' and t.idbranch='7699'";
//		List<Map<String,Object>>list = dataServiceAuto.getDataProcess().queryObjectList(new String[]{"accNo"},sq);
//		importDateFromOrcl(list,"c://邮寄1.txt");
	//	
//		sq="select accNo from EBS_ACCNOMAINDATA t where t.sendmode='1' and t.idbranch='7699'";
//		list = dataServiceAuto.getDataProcess().queryObjectList(new String[]{"accNo"},sq);
//		importDateFromOrcl(list,"c://柜台1.txt");
	//	
//		sq="select accNo from EBS_ACCNOMAINDATA t where t.sendmode='4' and t.idbranch='7699'";
//		list = dataServiceAuto.getDataProcess().queryObjectList(new String[]{"accNo"},sq);
		
	}
	
	//将数据导出到文本文件中
	public void importDateFromOrcl(List<Map<String,Object>> list,String fileName){
		BufferedOutputStream out = null;
		try{
			out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
			for(Map<String,Object> m:list){
				String accno =(String)m.get("accNo")+"\n";
				out.write(accno.getBytes());
			}
			out.flush();
			out.close();
		}catch(Exception e){
		}finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
