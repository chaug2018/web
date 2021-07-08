package com.yzj.ebs.ibank.server;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.ibank.server.XmlParser;
import com.yzj.wf.com.ibank.common.IBankDefine.Direction;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.com.ibank.common.template.Field;
import com.yzj.wf.com.ibank.common.template.FieldList;
import com.yzj.wf.com.ibank.common.template.Trade;


public class Trade2100 {

	/**
	 * 根据传入的trade对象和服务，获取必要的Field，处理后再返回处理后的trade
	 * @param trade	：经过解析后，Filed带value的trade
	 * @param object ：处理交易时将要使用的对象，如操作数据库的DAO等；
	 * @return trade : 处理后的trade，即将要返回的Field进行了赋值				--->修改为要返回的byte数组，也就是包封装报为字节数组的工作放在每个交易里面做，这样更便于具体交易的操作
	 * @throws IBankProcessException
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] execTrade(Trade trade,IBankAdm ibankAdm,StrToFile strToFile) throws IBankProcessException, CloneNotSupportedException, UnsupportedEncodingException {
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
//				if(field.getDirect()==Direction.UP.getValue()&& field.getValue()!=null){
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
					}
					if(field.getDirect()==Direction.UP_DOWN.getValue()&& field.getValue()!=null){
						doc.getRootElement().addElement(field.getName()).setText(field.getValue());
					}
				}
			}else{
				List<BasicInfo> list = null;
				list = ibankAdm.getBasicInfoByAccno(qryaccno);
				if(list!=null && list.size()>0){
					BasicInfo basicinfo = list.get(0);
					fieldlist.getFieldByName("SendType").setValue(basicinfo.getSendMode()==null?"":basicinfo.getSendMode());
					fieldlist.getFieldByName("Phone").setValue(basicinfo.getPhone()==null?"":basicinfo.getPhone());
					fieldlist.getFieldByName("PostCode").setValue(basicinfo.getZip()==null?"":basicinfo.getZip());
					fieldlist.getFieldByName("ContactName").setValue(basicinfo.getLinkMan()==null?"":basicinfo.getLinkMan());
					fieldlist.getFieldByName("Address").setValue(basicinfo.getAddress()==null?"":basicinfo.getAddress());
					
					//赋值要下传的Trade中的Filed.value
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
				}
				
				if(list==null || list.size()<1){
					fieldlist.getFieldByName("SendType").setValue("");
					fieldlist.getFieldByName("Phone").setValue("");
					fieldlist.getFieldByName("PostCode").setValue("");
					fieldlist.getFieldByName("ContactName").setValue("");
					fieldlist.getFieldByName("Address").setValue("");
					
					//赋值要下传的Trade中的Filed.value
					for(int i=0;i<fieldList.size();i++){
						Field field = fieldList.get(i);
						if(field.getDirect() == Direction.DOWN.getValue() && field.getValue()!=null){
							if(field.getName().equals("RSPCD")){	//在没有查询的数据的前提下，可以给出相应的提示
								field.setValue("0000");
								doc.getRootElement().addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("RSPMSG")){
								field.setValue("查询的账号不存在");
								doc.getRootElement().addElement(field.getName()).setText(field.getValue());
							}else{
								doc.getRootElement().addElement(field.getName()).setText(field.getValue());
							}
						}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
							doc.getRootElement().addElement(field.getName()).setText(field.getValue());
						}
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
		//System.out.println("2100响应：");
		//System.out.println(lenStr+upDataStr);
		//记载日志
		strToFile.creatFile(lenStr+upDataStr);
		upData = (lenStr + upDataStr).getBytes("UTF-8");
		return upData;
	}
}
