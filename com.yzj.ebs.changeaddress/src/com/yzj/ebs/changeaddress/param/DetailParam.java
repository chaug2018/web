package com.yzj.ebs.changeaddress.param;

import java.util.LinkedHashMap;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;

public class DetailParam extends PageParam {
	//每页显示条目数
	private Map<String, String> pageSizeMap=new LinkedHashMap<String,String>();
	//对账中心
	private String detailIDCenter;
	//发送模式
	private String detailSendMode;
	
	public DetailParam(){
		detailIDCenter = "";
		detailSendMode = "";
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

	public String getDetailIDCenter() {
		return detailIDCenter;
	}

	public void setDetailIDCenter(String detailIDCenter) {
		this.detailIDCenter = detailIDCenter;
	}

	public String getDetailSendMode() {
		return detailSendMode;
	}

	public void setDetailSendMode(String detailSendMode) {
		this.detailSendMode = detailSendMode;
	}
}
