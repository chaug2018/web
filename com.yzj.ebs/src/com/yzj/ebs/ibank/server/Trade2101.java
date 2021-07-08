package com.yzj.ebs.ibank.server;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.ebs.ibank.client.IBankClient;
import com.yzj.ebs.ibank.server.XmlParser;
import com.yzj.wf.com.ibank.common.IBankDefine.Direction;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.com.ibank.common.template.Field;
import com.yzj.wf.com.ibank.common.template.FieldList;
import com.yzj.wf.com.ibank.common.template.Trade;


public class Trade2101 {

	/**
	 * 根据传入的trade对象和服务，获取必要的Field，处理后再返回处理后的trade
	 * @param trade	：经过解析后，Filed带value的trade
	 * @param object ：处理交易时将要使用的对象，如操作数据库的DAO等；
	 * @return trade : 处理后的trade，即将要返回的Field进行了赋值				--->修改为要返回的byte数组，也就是包封装报为字节数组的工作放在每个交易里面做，这样更便于具体交易的操作
	 * @throws IBankProcessException
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] execTrade(Trade trade,IBankAdm ibankAdm,StrToFile strToFile,
			IBankClient iBankClient,IPublicTools publicTools) throws IBankProcessException, CloneNotSupportedException, UnsupportedEncodingException {
		byte[] upData = null ;
		/*
		 * 1、获取传入的交易Trade信息和服务，为查询操作准备条件；
		 * 2、将查询到的信息存入Trade中；
		 * 3、将Trade信息存入Document中，并将其转化为byte[]；
		 */
		
		//查询账号，单个查询时用来保存账号的临时变量
		String qryaccno="";				
		//查询数量，以此判断为单个查询还是批量查询
		int qryNo = 1;
		FieldList fieldlist = trade.getFieldList();
		List<Field> fieldList = fieldlist.getFields();
		//创建要返回的doc
		Document doc = XmlParser.createXML("Root");
		//创建字符流
		StringWriter stringWriter = new StringWriter();
		//创建输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		//创建xml写出流
		XMLWriter xmlWriter = new XMLWriter(stringWriter,format);
		
		if(qryNo==1){
			/*
			 * 单个查询处理逻辑：
			 * 1、截取单个账户和查询日期信息；
			 * 2、调用单账户查询服务；
			 * 3、将单账户的查询结果填充至Trade；
			 * 4、将Trade转化为Doc，再转化为String和byte[]
			 */
//			for(Field field :fieldList){
//				if(field.getDirect()==Direction.UP.getValue()){
//					if(field.getName().equals("AcNo")){
//						qryaccno = field.getValue().trim();
//					}
//				}
//			}
			if(fieldlist.getFieldByName("AcNo").getValue()!=null 
					&& !(fieldlist.getFieldByName("AcNo").getValue().equals(""))){
					qryaccno = fieldlist.getFieldByName("AcNo").getValue().trim();
			}
			
			if(qryaccno.equals("")){
				for(Field field :fieldList){
					if(field.getDirect()==Direction.DOWN.getValue()&& field.getValue()!=null){
						if(field.getName().equals("RSPCD")){
							field.setValue("fail");
							doc.getRootElement().addElement(field.getName()).setText(field.getValue());
						}else if(field.getName().equals("RSPMSG")){
							field.setValue("账号不能为空");
							doc.getRootElement().addElement(field.getName()).setText(field.getValue());
						}else{
							doc.getRootElement().addElement(field.getName()).setText(field.getValue());
						}
					}else if(field.getDirect()==Direction.UP_DOWN.getValue()&& field.getValue()!=null){
						doc.getRootElement().addElement(field.getName()).setText(field.getValue());
					}
				}
			}else{
				//查询服务：
				List<BasicInfo> list = null;
				list = ibankAdm.getBasicInfoByAccno(qryaccno);
				
				if(list!=null && list.size()>0){
					String result = "";
					BasicInfo basicinfo = list.get(0);
					//签约和修改的发送方式写死为网银
					basicinfo.setSendMode("3");
					basicinfo.setSignFlag("1");//设置签约标志 0：柜面签约网银 1:网银接口签约网银
					//该账号设置为对账
					basicinfo.setIsCheck("0");
					basicinfo.setPhone(fieldlist.getFieldByName("Phone").getValue());
					basicinfo.setZip(fieldlist.getFieldByName("PostCode").getValue());
					basicinfo.setLinkMan(fieldlist.getFieldByName("ContactName").getValue());
					basicinfo.setAddress(fieldlist.getFieldByName("Address").getValue());
					//同时更新发送地址
					basicinfo.setSendAddress(fieldlist.getFieldByName("Address").getValue());
					
					if(basicinfo!=null){
						result = ibankAdm.updateBasicInfoByEntry(basicinfo);
					}
					if(result.equals("success")){
						for(int i=0;i<fieldList.size();i++){
							Field field = fieldList.get(i);
							if(field.getDirect() == Direction.DOWN.getValue()&& field.getValue()!=null){
								if(field.getName().equals("RSPCD")){	//在没有查询的数据的前提下，可以给出相应的提示
									field.setValue("0000");
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}else if(field.getName().equals("RSPMSG")){
									field.setValue("trade success");
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}else{
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}
							}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
								doc.getRootElement().addElement(field.getName()).setText(field.getValue());
							}
						}
					}else{
						for(int i=0;i<fieldList.size();i++){
							Field field = fieldList.get(i);
							if(field.getDirect() == Direction.DOWN.getValue()&& field.getValue()!=null){
								if(field.getName().equals("RSPCD")){	//在没有查询的数据的前提下，可以给出相应的提示
									field.setValue("fail");
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}else if(field.getName().equals("RSPMSG")){
									if(fieldlist.getFieldByName("SignType").getValue().equals("0")){
										field.setValue("签约失败");
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}else{
										field.setValue("修改失败");
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}
								}else{
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}
							}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
								doc.getRootElement().addElement(field.getName()).setText(field.getValue());
							}
						}
					}
				}
				if(list==null || list.size()<1){
						String res = "";
						BasicInfo basicin = new BasicInfo();
						basicin.setAccNo(fieldlist.getFieldByName("AcNo").getValue());
						//签约和修改的发送方式写死为网银
						basicin.setSendMode("3");
						basicin.setSignFlag("1");//设置签约标志 0：柜面签约网银 1:网银接口签约网银
						//该账号设置为对账
						basicin.setIsCheck("0");
						//插入时,设置该账号为普通账号
						basicin.setIsSpecile("0");
						//按照maindata迁移到basicinfo时,初始化字段
						basicin.setFaceFlag("0");
						basicin.setSpecialFlag("0");
						basicin.setDistributary("0");
						
						basicin.setPhone(fieldlist.getFieldByName("Phone").getValue());
						basicin.setZip(fieldlist.getFieldByName("PostCode").getValue());
						basicin.setLinkMan(fieldlist.getFieldByName("ContactName").getValue());
						basicin.setAddress(fieldlist.getFieldByName("Address").getValue());
						//同时更新发送地址
						basicin.setSendAddress(fieldlist.getFieldByName("Address").getValue());
						
						if(basicin!=null){
							res = ibankAdm.insertBasicInfoByEntry(basicin);
						}
						if(res.equals("success")){
							for(int i=0;i<fieldList.size();i++){
								Field field = fieldList.get(i);
								if(field.getDirect() == Direction.DOWN.getValue()&& field.getValue()!=null){
									if(field.getName().equals("RSPCD")){	//在没有查询的数据的前提下，可以给出相应的提示
										field.setValue("0000");
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}else if(field.getName().equals("RSPMSG")){
										field.setValue("trade success");
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}else{
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}
								}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}
							}
						}else{
							for(int i=0;i<fieldList.size();i++){
								Field field = fieldList.get(i);
								if(field.getDirect() == Direction.DOWN.getValue()&& field.getValue()!=null){
									if(field.getName().equals("RSPCD")){	//在没有查询的数据的前提下，可以给出相应的提示
										field.setValue("fail");
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}else if(field.getName().equals("RSPMSG")){
										if(fieldlist.getFieldByName("SignType").getValue().equals("0")){
											field.setValue("签约失败");
											doc.getRootElement().addElement(field.getName()).setText(field.getValue());
										}else{
											field.setValue("修改失败");
											doc.getRootElement().addElement(field.getName()).setText(field.getValue());
										}
									}else{
										doc.getRootElement().addElement(field.getName()).setText(field.getValue());
									}
								}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
									doc.getRootElement().addElement(field.getName()).setText(field.getValue());
								}
							}		    	
						}
					}
				//签约成功向CIF发送签约标识
				String RSPCD = fieldlist.getFieldByName("RSPCD").getValue();
				String SEQNO = fieldlist.getFieldByName("SEQNO").getValue();
				String RSPMSG = fieldlist.getFieldByName("RSPMSG").getValue();
				
				String OPDESC = "流水号: "+SEQNO+" -- "+RSPMSG;
				SimpleDateFormat fromatDateFormat  =new SimpleDateFormat("yyyyMMdd");
				String  opDate = fromatDateFormat.format(new Date());
				
				if(RSPCD!=null && RSPCD.equals("0000")){
					String SignType = fieldlist.getFieldByName("SignType").getValue();
					if(SignType!=null && SignType.equals("0")){
						//在数据库中记载签约日志
						BasicInfoLog basicInfoLog = publicTools.getDataLogUtils().creatBasicInfoLog("签约"+OPDESC,String.valueOf(OperLogModuleDefine.IBANKTRACE),opDate);
						basicInfoLog.setAccNo(qryaccno);
						try {
							publicTools.getDataLogUtils().createBasicInfoLog(basicInfoLog);
						} catch (XDocProcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//签约成功向CIF发送签约标识
						iBankClient.startByAccno(qryaccno);
					}else if(SignType!=null && SignType.equals("1")){
						//在数据库中记载修改日志
						BasicInfoLog basicInfoLog = publicTools.getDataLogUtils().creatBasicInfoLog("修改"+OPDESC,String.valueOf(OperLogModuleDefine.IBANKTRACE),opDate);
						basicInfoLog.setAccNo(qryaccno);
						try {
							publicTools.getDataLogUtils().createBasicInfoLog(basicInfoLog);
						} catch (XDocProcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else if(RSPCD!=null && RSPCD.equals("fail")){
					String SignType = fieldlist.getFieldByName("SignType").getValue();
					if(SignType!=null && SignType.equals("0")){
						//在数据库中记载签约日志
						BasicInfoLog basicInfoLog = publicTools.getDataLogUtils().creatBasicInfoLog("签约"+OPDESC,String.valueOf(OperLogModuleDefine.IBANKTRACE),opDate);
						basicInfoLog.setAccNo(qryaccno);
						try {
							publicTools.getDataLogUtils().createBasicInfoLog(basicInfoLog);
						} catch (XDocProcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(SignType!=null && SignType.equals("1")){
						//在数据库中记载修改日志
						BasicInfoLog basicInfoLog = publicTools.getDataLogUtils().creatBasicInfoLog("修改"+OPDESC,String.valueOf(OperLogModuleDefine.IBANKTRACE),opDate);
						basicInfoLog.setAccNo(qryaccno);
						try {
							publicTools.getDataLogUtils().createBasicInfoLog(basicInfoLog);
						} catch (XDocProcException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		    }
			try {
				xmlWriter.write(doc);
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//将字符串转化为字节数组
			String upDataStr = stringWriter.toString();
			//将报文长度转化为8位的十进制数
			String lenStr = Integer.toString(upDataStr.getBytes("UTF-8").length); 
			while(lenStr.length() < 8){
				lenStr = "0" + lenStr;
			}
			//System.out.println("2101响应：");
			//System.out.println(lenStr+upDataStr);
			//记载日志
			strToFile.creatFile(lenStr+upDataStr);
			
			try {
				upData = (lenStr + upDataStr).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return upData;
	}
}

