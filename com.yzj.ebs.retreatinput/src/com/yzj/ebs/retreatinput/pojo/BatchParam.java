package com.yzj.ebs.retreatinput.pojo;

import java.io.Serializable;
import java.util.List;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2013-04-03<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 批量登记   bean
 * @author 单伟龙
 * @version 1.0.0
 */
public class BatchParam extends PageParam implements Serializable{

	/**
	 * 以下是目前需求中需要的字段，还可以在里面拓展出不能跨机构的机构号信息，还有柜员信息等等
	 */
	private static final long serialVersionUID = 1L;
	private int successCount=0;// 登记成功数
	private int failCount=0;//  登记失败数
	private String msg;//错误信息
	private List<ImportData> failList;
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public List<ImportData> getFailList() {
		return failList;
	}
	public void setFailList(List<ImportData> failList) {
		this.failList = failList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
