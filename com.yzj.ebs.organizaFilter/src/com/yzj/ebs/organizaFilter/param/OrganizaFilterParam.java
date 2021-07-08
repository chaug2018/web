package com.yzj.ebs.organizaFilter.param;

import java.util.LinkedHashMap;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;


public class OrganizaFilterParam extends PageParam{
	//每页显示条目数
	private Map<String, String> pageSizeMap=new LinkedHashMap<String,String>();
	
	//机构号
	private String idBank;
	
	//对账中心
	private String idCenter;
	
	public OrganizaFilterParam(){
		idBank="";
		idCenter="";
		pageSizeMap.put("20","20");
		pageSizeMap.put("50","50");
		pageSizeMap.put("100","100");
	}
	
	public Map<String, String> getPageSizeMap() {
		return pageSizeMap;
	}

	public void setPageSizeMap(Map<String, String> pageSizeMap) {
		this.pageSizeMap = pageSizeMap;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
