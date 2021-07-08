package com.yzj.ebs.ibank.client;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.ibank.server.IBankAdm;
import com.yzj.ebs.ibank.server.XmlParser;
import com.yzj.wf.com.ibank.common.IBankDefine.Direction;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.com.ibank.common.template.Field;
import com.yzj.wf.com.ibank.common.template.FieldList;
import com.yzj.wf.com.ibank.common.template.Trade;


public class Trade2103 {

	/**
	 * 根据传入的trade对象和服务，获取必要的Field，处理后再返回处理后的trade
	 * @param trade	：经过解析后，Filed带value的trade
	 * @param object ：处理交易时将要使用的对象，如操作数据库的DAO等；
	 * @return trade : 处理后的trade，即将要返回的Field进行了赋值				--->修改为要返回的byte数组，也就是包封装报为字节数组的工作放在每个交易里面做，这样更便于具体交易的操作
	 * @throws IBankProcessException
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] execTrade(Trade trade,IBankAdm ibankAdm,String accno,ClientToFile clientToFile) throws IBankProcessException, CloneNotSupportedException, UnsupportedEncodingException {
		byte[] upData = null ;
		/*
		 * 1、获取传入的交易Trade信息和服务，为查询操作准备条件；
		 * 2、将查询到的信息存入Trade中；
		 * 3、将Trade信息存入Document中，并将其转化为byte[]；
		 */
		
		FieldList fieldlist = trade.getFieldList();
		List<Field> fieldList = fieldlist.getFields();
		//创建要返回的doc
		Document doc = XmlParser.createXML("Transaction");
		Element root = doc.getRootElement();
		Element Transaction_Header = root.addElement("Transaction_Header");
		Element Transaction_Body= root.addElement("Transaction_Body");
//		Element ext_attributes = Transaction_Header.addElement("ext_attributes");
		Element request = Transaction_Body.addElement("request");
		//创建字符流
		StringWriter stringWriter = new StringWriter();
		//创建输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		//创建xml写出流
		XMLWriter xmlWriter = new XMLWriter(stringWriter,format);
		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfMs = new SimpleDateFormat("HHmmss");
		Date date = new Date();
		String dateStr = sdfDay.format(date);
		String time = sdfMs.format(date);
		String uuid = UUID.randomUUID().toString()
				.replace("-", "").toUpperCase();
		
		if(accno!=null && !accno.equals("")){
				List<BasicInfo> list = null;
				list = ibankAdm.getBasicInfoByAccno(accno.trim());
				if(list!=null && list.size()>0){
					BasicInfo basicinfo = list.get(0);
					fieldlist.getFieldByName("AcNo").setValue(basicinfo.getAccNo()==null?"":basicinfo.getAccNo());
					if(basicinfo.getIsCheck()!=null && !basicinfo.getIsCheck().equals("")){
						fieldlist.getFieldByName("SignCheck").setValue(basicinfo.getIsCheck().equals("0")?"1":"2");
						fieldlist.getFieldByName("SignStatus").setValue(basicinfo.getIsCheck().equals("0")?"00":"02");
					}
					
					//赋值要下传的Trade中的Filed.value
					for(int i=0;i<fieldList.size();i++){
						Field field = fieldList.get(i);
						if(field.getDirect() == Direction.UP.getValue()&& field.getValue()!=null){
							if(field.getName().equals("transaction_sn")){	//在没有查询的数据的前提下，可以给出相应的提示
								field.setValue(uuid);
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_id")){
								field.setValue("E001");
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_inst")){
								field.setValue("");
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_opr")){
								field.setValue("");
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("requester_id")){
								field.setValue("WEBEB");
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_date")){
								field.setValue(dateStr);
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_time")){
								field.setValue(time);
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("AcNo")){
								request.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("SignCheck")){
								request.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("SignStatus")){
								request.addElement(field.getName()).setText(field.getValue());
							}
						}else if(field.getDirect() == Direction.UP_DOWN.getValue()&& field.getValue()!=null){
							if(field.getName().equals("transaction_sn")){	//在没有查询的数据的前提下，可以给出相应的提示
								field.setValue(uuid);
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}else if(field.getName().equals("transaction_id")){
								field.setValue("E001");
								Transaction_Header.addElement(field.getName()).setText(field.getValue());
							}
						}
					}
				}else{
					return null;
				}
		    }
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//将stringWriter转化为字符串
		String upDataStr = stringWriter.toString();
		//将报文长度转化为8位的十进制数
		String lenStr = Integer.toString(upDataStr.getBytes("UTF-8").length); 
		while(lenStr.length() < 8){
				lenStr = "0" + lenStr;
		}
		//System.out.println("E001请求：");
		//System.out.println(lenStr+upDataStr);
		//记载日志
		clientToFile.creatFile(lenStr+upDataStr);
		try {
			upData = (lenStr + upDataStr).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return upData;
	}
}
