package com.yzj.ebs.changeaddress.param;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;

public class DataParam  extends PageParam implements Serializable{
	
	//序列化
	private static final long serialVersionUID = 1L;
	//每页显示条目数
	private Map<String, String> pageSizeMap=new LinkedHashMap<String,String>();
	//对账中心
	private String idCenter;
	//发送方式
	private String sendMode;
	//投递地址
	private String sendAddress;
	
	//网点
	private String idBank;
	
	public DataParam(){
		pageSizeMap.put("20","20");
		pageSizeMap.put("50","50");
		pageSizeMap.put("100","100");
		
		this.idCenter = "";
		this.sendMode = "";
		this.sendAddress = "";
		this.idBank = "";
	}

	public Map<String, String> getPageSizeMap() {
		return pageSizeMap;
	}

	public void setPageSizeMap(Map<String, String> pageSizeMap) {
		this.pageSizeMap = pageSizeMap;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getSendMode() {
		return sendMode;
	}

	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	
	
	
	
}
