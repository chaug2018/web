package com.yzj.ebs.report.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 
 *创建于:2012-11-12<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 连续对账未成功账户明细(未覆盖的明细)查询条件类
 * @author 施江敏
 * @version 1.0.0
 */
public class CoverFailQueryParam extends PageParam implements Serializable{
	private static final long serialVersionUID = 1L;
	//对账中心
	private String idCenter;
	//支行
	private String idBranch;
	//网点
	private String idBank;
	//网点
	private String idBank1;

	//对账开始日期
	private String beginDocDate;
	//对账开始日期
	private String endDocDate;
		
	//机构类型
	private String orgType;
	
	//发送方式
	private String sendMode;
	//账户类型
	private String accCycle;
	
	//未成功次数
	private String failCount;
		
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getIdBank1() {
		return idBank1;
	}
	public void setIdBank1(String idBank1) {
		this.idBank1 = idBank1;
	}
	public String getBeginDocDate() {
		return beginDocDate;
	}
	public void setBeginDocDate(String beginDocDate) {
		this.beginDocDate = beginDocDate;
	}
	public String getEndDocDate() {
		return endDocDate;
	}
	public void setEndDocDate(String endDocDate) {
		this.endDocDate = endDocDate;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	public String getFailCount() {
		return failCount;
	}
	public void setFailCount(String failCount) {
		this.failCount = failCount;
	}
	

}
