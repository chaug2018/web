package com.yzj.ebs.ebill.analyse.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 
 *创建于:2012-11-12<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计查询条件类
 * @author 施江敏
 * @version 1.0.0
 */
public class QueryParam extends PageParam implements Serializable{
	private static final long serialVersionUID = -6839231223044677412L;
	//对账中心
	private String idCenter;
	//支行
	private String idBranch;
	//网点
	private String idBank;
	//网点
	private String idBank1;
	//对账日期
	private String docDate;
	//机构类型
	private String orgType;
	//客户号
	private String custId;
	//账户
	private String accNo;
	//客户经理
	private String custManager;
	//发送方式
	private String sendMode;
	//账户类型
	private String accCycle;
	
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
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getCustManager() {
		return custManager;
	}
	public void setCustManager(String custManager) {
		this.custManager = custManager;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
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
	

}
