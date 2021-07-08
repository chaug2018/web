package com.yzj.ebs.ibank.server;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.yzj.ebs.ibank.server.XmlParser;
import com.yzj.wf.com.ibank.common.IBankAssemble;
import com.yzj.wf.com.ibank.common.IBankAssembleException;
import com.yzj.wf.com.ibank.common.IBankDefine.Direction;
import com.yzj.wf.com.ibank.common.template.Field;
import com.yzj.wf.com.ibank.common.template.FieldList;
import com.yzj.wf.com.ibank.common.template.Trade;

public class IBankAssembleService implements IBankAssemble {

	public Trade trade;
	/**
	 * 报文信息的解析
	 * 根据传过来的交易模板和byte数组报文进行解析，将解析后的数据保存到trade对象中
	 */
	@Override
	public void downAssemble(Trade trade, byte[] dataDown)
			throws IBankAssembleException {
		//将上传的报文解析到对应的Trade对象中，并保存起来共其他对象调用
		if(dataDown.length != 0){
			String dataDownStr = new String(dataDown, 0, dataDown.length);
			FieldList fieldlist = trade.getFieldList();
			List<Field> fieldList = fieldlist.getFields();
			String name  = "";
			String value = "";
			for(int i=0;i<fieldList.size();i++){
				Field field = fieldList.get(i);
				if(field.getDirect()== Direction.UP.getValue()
						|| field.getDirect() == Direction.UP_DOWN.getValue() ){				//仅需上传的公共字段
					name = field.getName();
					value = getValueByName(dataDownStr,name);
					field.setValue(value);
				}
			}
		}
		
		this.setTrade(trade);
	}
	
	public void downAssemble(Trade trade, String dataDownStr)
			throws IBankAssembleException {
		if(dataDownStr!=null && !dataDownStr.equals("")){
			FieldList fieldlist = trade.getFieldList();
			List<Field> fieldList = fieldlist.getFields();
			String name  = "";
			String value = "";
			for(int i=0;i<fieldList.size();i++){
				Field field = fieldList.get(i);
				if(field.getDirect()== Direction.UP.getValue()
						|| field.getDirect() == Direction.UP_DOWN.getValue() ){				//仅需上传的公共字段
					name = field.getName();
					value = getValueByName(dataDownStr,name);
					field.setValue(value);
				}
			}
		}
		
		this.setTrade(trade);
	}

	/**
	 * 报文信息的装配
	 */
	@Override
	public byte[] upAssemble(Trade trade) throws IBankAssembleException {
		byte[] dataUp = new byte[3000];

		//将upParamsMap中的字段转化成xml字符串；
		Document doc = XmlParser.createXML("Root");
		FieldList fieldList = trade.getFieldList();
		List<Field> fields = fieldList.getFields();
		for(Field field:fields){
			if(field.getDirect()==Direction.DOWN.getValue() 
					|| field.getDirect() == Direction.UP_DOWN.getValue()){	//填充仅下传的字段
				XmlParser.createElement(doc.getRootElement(), field.getName(),field.getValue() );
			}
		}
		
		StringWriter stringWriter = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(stringWriter,format);
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//将Document转化为String
		String dataUpStr = stringWriter.toString();
		System.out.println("下传的报文为：");
		System.out.println(dataUpStr);
		dataUp = dataUpStr.getBytes();
		return dataUp;
	}

	/**
	 * 根据
	 * @param dataDownStr:接收到得报文字符串
	 * @param name:字段名
	 * @return	value 从报文字符串中截取到的字段值
	 */
	private String getValueByName(String dataDownStr,String name){
		String value = "";
		String	prefix = "<"+name+">";
		String	suffix = "</"+name+">";
		if(dataDownStr.contains(name)){
			value = dataDownStr.substring(dataDownStr.indexOf(prefix)+prefix.length(),dataDownStr.indexOf(suffix)).trim();
		}
		return value;
	}
	
	/**
	 * 从指定的xml报文字符串中获取同名标签的不同值
	 * @param dataDownStr	接收到的报文字符串
	 * @param name			要查找的标签名
	 * @param no			要查找的数量
	 * @return
	 */
	private Map<Integer,String> getValueMapByName(String dataDownStr,String name,int no ){
		Map<Integer,String> valueMap = new HashMap<Integer,String>();
		String value = null;
		String	prefix = "<"+name+">";
		String	suffix = "</"+name+">";
		String tempStr = dataDownStr;		//暂存报文字符串
		for(int i=0;i<no;i++){
			if(dataDownStr.contains(name)){
				int beginIndex = tempStr.indexOf(prefix)+prefix.length(); 
				int endIndex = tempStr.indexOf(suffix);
				value = tempStr.substring(beginIndex, endIndex);	//截取所需子字符串
				tempStr = tempStr.substring(endIndex+suffix.length(), tempStr.length());	//斩断已截取的，便于继续截取
				valueMap.put(new Integer(i), value);
			}
		}
		return valueMap;
	}
	
	/**
	 * 计算某一子字符串在母字符串中的数量
	 * @param parantStr	：母字符串
	 * @param sonStr	：子字符串
	 * @return	存在数
	 */
	public int intOfStr (String parantStr,String sonStr){
		int num = 0;
		String tempStr = parantStr;		//暂存报文字符串
		do{
			if(tempStr.contains(sonStr)){
				int endIndex = tempStr.indexOf(sonStr)+sonStr.length();	//截取的终止位置
				tempStr = tempStr.substring(endIndex, tempStr.length());//去掉已经判断的部分，便于后续再判断
				num ++;
			}
		}while(tempStr.contains(sonStr));
		return num;
	}
	
	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}
	
}
